package com.flyingwillow.tdd.service;

import com.flyingwillow.tdd.util.HttpMethodEnum;
import com.flyingwillow.tdd.util.ToolkitUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class DefaultInterfaceNameReader implements InterfaceNameReader{

    public static final String API_OPERATION = "io.swagger.annotations.ApiOperation";
    public static final String ATTRIBUTE_VALUE = "value";
    public static final String API = "io.swagger.annotations.Api";
    public static final String REQUEST_MAPPING = "org.springframework.web.bind.annotation.RequestMapping";

    public static final String ANNOTATION_GET_MAPPING = "org.springframework.web.bind.annotation.GetMapping";
    public static final String ANNOTATION_POST_MAPPING = "org.springframework.web.bind.annotation.PostMapping";
    public static final String ANNOTATION_PUT_MAPPING = "org.springframework.web.bind.annotation.PutMapping";
    public static final String ANNOTATION_DELETE_MAPPING = "org.springframework.web.bind.annotation.DeleteMapping";
    public static final Set<String> SUPPORTED_TYPE = ImmutableSet.of(
            ANNOTATION_GET_MAPPING,
            ANNOTATION_POST_MAPPING,
            ANNOTATION_PUT_MAPPING,
            ANNOTATION_DELETE_MAPPING,
            REQUEST_MAPPING
    );

    public static final Map<String,HttpMethodEnum> methodMap = ImmutableMap.<String,HttpMethodEnum>builder()
            .put(ANNOTATION_GET_MAPPING, HttpMethodEnum.GET)
            .put(ANNOTATION_POST_MAPPING, HttpMethodEnum.PUT)
            .put(ANNOTATION_PUT_MAPPING, HttpMethodEnum.PUT)
            .put(ANNOTATION_DELETE_MAPPING, HttpMethodEnum.DELETE)
            .build();

    @Override
    public String getPackageName(PsiJavaFile file) {
        final String packageName = file.getPackageName();
        return packageName.substring(packageName.lastIndexOf("\\.")+1);
    }

    @Override
    public String getPackageId(PsiJavaFile file) {
        return file.getPackageName();
    }

    @Override
    public String getControllerName(PsiClass cls) {
        String name = cls.getName();
        final PsiAnnotation annotation = cls.getAnnotation(API);
        if(Objects.nonNull(annotation)){
            final PsiAnnotationMemberValue tags = annotation.findAttributeValue("tags");
            final PsiAnnotationMemberValue value = annotation.findAttributeValue(ATTRIBUTE_VALUE);
            if(Objects.nonNull(tags)){
                name = ToolkitUtil.parseStringValue(tags);
            } else if(Objects.nonNull(value)){
                name = ToolkitUtil.parseStringValue(value);
            }
        }
        return name;
    }

    @Override
    public String getControllerId(PsiClass cls) {
        return cls.getQualifiedName();
    }

    @Override
    public String getInterfaceName(PsiMethod method, PsiClass file) {
        final PsiAnnotation operation = method.getAnnotation(API_OPERATION);
        if(Objects.nonNull(operation) && operation.hasAttribute(ATTRIBUTE_VALUE)){
            return ToolkitUtil.parseStringValue(operation.findAttributeValue(ATTRIBUTE_VALUE));
        } else {
            return method.getName();
        }
    }

    @Override
    public String getInterfaceId(PsiMethod method, PsiClass cls) {
        StringBuilder sb = new StringBuilder();
        final PsiAnnotation clsMapping = cls.getAnnotation(REQUEST_MAPPING);
        if(Objects.nonNull(clsMapping) && StringUtils.isNotEmpty(clsMapping.findAttributeValue(ATTRIBUTE_VALUE).getText())){
            String value = ToolkitUtil.parseStringValue(clsMapping.findAttributeValue(ATTRIBUTE_VALUE));
            if(!value.startsWith("/")){
                sb.append("/");
            }
            sb.append(value);
        }
        final Optional<PsiAnnotation> first = Arrays.stream(method.getAnnotations()).filter(ann -> SUPPORTED_TYPE.contains(ann.getQualifiedName()))
                .findFirst();
        if(first.isPresent()){
            final String path = ToolkitUtil.parseStringValue(first.get().findAttributeValue(ATTRIBUTE_VALUE));
            if(!path.startsWith("/")){
                sb.append("/");
            }
            sb.append(path);
        }
        if(sb.lastIndexOf("/") == sb.length()-1){
            sb.deleteCharAt(sb.lastIndexOf("/")-1);
        }

        return sb.toString().replaceAll("//", "/");
    }

    @Override
    public boolean isValidInterface(PsiMethod method) {
        return Arrays.stream(method.getAnnotations()).anyMatch(psiAnnotation -> SUPPORTED_TYPE.contains(psiAnnotation.getQualifiedName()));
    }

    @Override
    public PsiClass getMainClass(PsiJavaFile javaFile) {

        return Arrays.stream(javaFile.getClasses())
                .filter(cls -> javaFile.getName().contains(cls.getName())).findFirst().get();
    }

    @Override
    public String defaultPackage(String packagePath) {
        return null;
    }

    @Override
    public HttpMethodEnum getHttpMethod(PsiMethod method) {
        final Optional<PsiAnnotation> first = Arrays.stream(method.getAnnotations()).filter(ann -> SUPPORTED_TYPE.contains(ann.getQualifiedName()))
                .findFirst();
        if(!first.isPresent()){
            return null;
        } else if(methodMap.containsKey(first.get().getQualifiedName())){
            return methodMap.get(first.get().getQualifiedName());
        } else if(REQUEST_MAPPING.equals(first.get().getQualifiedName())){
            return judgeHttpMethod(first.get());
        } else {
            return null;
        }
    }

    private HttpMethodEnum judgeHttpMethod(PsiAnnotation mapping) {
        return HttpMethodEnum.valueOf(ToolkitUtil.parseStringValue(mapping.findAttributeValue("method")));
    }
}
