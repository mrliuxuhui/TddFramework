package com.flyingwillow.tdd.service;

import com.flyingwillow.tdd.util.ToolkitUtil;
import com.google.common.collect.ImmutableSet;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiMethod;
import com.intellij.util.text.LiteralFormatUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class DefaultInterfaceNameReader implements InterfaceNameReader{

    public static final String API_OPERATION = "io.swagger.annotations.ApiOperation";
    public static final String ATTRIBUTE_VALUE = "value";
    public static final String API = "io.swagger.annotations.Api";
    public static final String REQUEST_MAPPING = "org.springframework.web.bind.annotation.RequestMapping";

    public static final Set<String> SUPPORTED_TYPE = ImmutableSet.of(
            "org.springframework.web.bind.annotation.GetMapping",
            "org.springframework.web.bind.annotation.PostMapping",
            "org.springframework.web.bind.annotation.PutMapping",
            "org.springframework.web.bind.annotation.DeleteMapping",
            REQUEST_MAPPING
    );

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
    public String DefaultPackage(String packagePath) {
        return null;
    }
}
