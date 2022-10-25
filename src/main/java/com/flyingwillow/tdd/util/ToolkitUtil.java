package com.flyingwillow.tdd.util;

import com.flyingwillow.tdd.domain.FieldDataType;
import com.flyingwillow.tdd.domain.InterfaceMetaInfo;
import com.flyingwillow.tdd.service.ComponentSearchService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.CommonClassNames;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiArrayInitializerMemberValue;
import com.intellij.psi.PsiArrayType;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.tree.java.PsiArrayInitializerMemberValueImpl;
import com.intellij.psi.util.InheritanceUtil;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.psi.util.TypeConversionUtil;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.Producer;
import com.intellij.util.ui.tree.TreeUtil;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Future;

public class ToolkitUtil {

    private static final Set<PsiType> PRIMITIVE_TYPES = ImmutableSet.of(
            PsiType.INT,
            PsiType.BYTE,
            PsiType.CHAR,
            PsiType.DOUBLE,
            PsiType.FLOAT,
            PsiType.LONG,
            PsiType.SHORT,
            PsiType.BOOLEAN
    );

    private static final Map<PsiPrimitiveType, FieldDataType> PRIMITIVE_MAP = ImmutableMap.<PsiPrimitiveType, FieldDataType>builder()
            .put(PsiType.INT, FieldDataType.INTEGER)
            .put(PsiType.BYTE, FieldDataType.SHORT)
            .put(PsiType.SHORT, FieldDataType.SHORT)
            .put(PsiType.CHAR, FieldDataType.STRING)
            .put(PsiType.DOUBLE, FieldDataType.DOUBLE)
            .put(PsiType.LONG, FieldDataType.LONG)
            .put(PsiType.BOOLEAN, FieldDataType.BOOLEAN)
            .build();
    private static final Set<String> BOXED_TYPES = ImmutableSet.of(
            CommonClassNames.JAVA_LANG_BOOLEAN,
            CommonClassNames.JAVA_LANG_CHARACTER,
            CommonClassNames.JAVA_LANG_CHAR_SEQUENCE,
            CommonClassNames.JAVA_LANG_STRING,
            CommonClassNames.JAVA_LANG_STRING_BUFFER,
            CommonClassNames.JAVA_LANG_STRING_BUILDER,
            CommonClassNames.JAVA_LANG_BYTE,
            CommonClassNames.JAVA_LANG_SHORT,
            CommonClassNames.JAVA_LANG_INTEGER,
            CommonClassNames.JAVA_LANG_LONG,
            CommonClassNames.JAVA_LANG_FLOAT,
            CommonClassNames.JAVA_LANG_DOUBLE
    );

    private static final Map<String, FieldDataType> BOXED_MAP = ImmutableMap.<String,FieldDataType>builder()
            .put(CommonClassNames.JAVA_LANG_BOOLEAN, FieldDataType.BOOLEAN)
            .put(CommonClassNames.JAVA_LANG_BYTE, FieldDataType.SHORT)
            .put(CommonClassNames.JAVA_LANG_SHORT, FieldDataType.SHORT)
            .put(CommonClassNames.JAVA_LANG_INTEGER, FieldDataType.INTEGER)
            .put(CommonClassNames.JAVA_LANG_LONG, FieldDataType.LONG)
            .put(CommonClassNames.JAVA_LANG_FLOAT, FieldDataType.FLOAT)
            .put(CommonClassNames.JAVA_LANG_DOUBLE, FieldDataType.DOUBLE)
            .put(CommonClassNames.JAVA_LANG_CHARACTER, FieldDataType.STRING)
            .put(CommonClassNames.JAVA_LANG_CHAR_SEQUENCE, FieldDataType.STRING)
            .put(CommonClassNames.JAVA_LANG_STRING, FieldDataType.STRING)
            .put(CommonClassNames.JAVA_LANG_STRING_BUFFER, FieldDataType.STRING)
            .put(CommonClassNames.JAVA_LANG_STRING_BUILDER, FieldDataType.STRING)
            .build();
    private ToolkitUtil() {

    }

    public static String parseStringValue(PsiAnnotationMemberValue value) {
        if(Objects.isNull(value)){
            return "";
        }else if (value instanceof PsiLiteralExpression) {
            return String.valueOf(((PsiLiteralExpression) value).getValue());
        } else if (value instanceof PsiArrayInitializerMemberValue) {
            return String.valueOf(((PsiLiteralExpression) ((PsiArrayInitializerMemberValueImpl) value).getInitializers()[0]).getValue());
        } else {
            return value.getText();
        }
    }

    public static String parseValue(PsiAnnotationMemberValue value) {
        return null;
    }

    public static InterfaceMetaInfo getMetaFromActionEvent(AnActionEvent event) {
        final ComponentSearchService service = event.getProject().getService(ComponentSearchService.class);
        final Tree interfaceTree = service.getContent().getInterfaceTree();
        final TreePath treePath = TreeUtil.getSelectedPathIfOne(interfaceTree);
        if (Objects.isNull(treePath)) {
            return null;
        }
        final InterfaceMetaInfo metaInfo = (InterfaceMetaInfo) TreeUtil.getLastUserObject(treePath);
        if (Objects.nonNull(metaInfo)) {
            return metaInfo;
        }
        return null;
    }

    public static <T> Future<T> asyncRead(Producer<T> producer, Project project){
        return ApplicationManager.getApplication().executeOnPooledThread(() -> {
            DumbService.getInstance(project).waitForSmartMode();
            return ApplicationManager.getApplication().runReadAction(
                    (Computable<T>) () -> producer.produce());
        });
    }

    public static boolean isSimpleType(PsiType type){
        if(isPrimitiveType(type)){
            return true;
        } else if(BOXED_TYPES.stream().anyMatch(e -> type.equalsToText(e))){
            return true;
        }
        return false;
    }

    public static boolean isPrimitiveType(PsiType type){
        return PRIMITIVE_TYPES.contains(type);
    }

    public static boolean isArrayType(PsiType type){
        return type instanceof PsiArrayType;
    }

    public static boolean isListableType(PsiType type){
        return  isArrayType(type)
                || InheritanceUtil.isInheritor(type, CommonClassNames.JAVA_UTIL_LIST)
                || InheritanceUtil.isInheritor(type, CommonClassNames.JAVA_UTIL_SET);
    }

    public static boolean isMapType(PsiType type){
        return  InheritanceUtil.isInheritor(type, CommonClassNames.JAVA_UTIL_MAP);
    }

    public static FieldDataType getDataType(PsiType type){
        if(isPrimitiveType(type)){
            return PRIMITIVE_MAP.get(type);
        }else if(isListableType(type)){
            return FieldDataType.LIST;
        } else if(isMapType(type)){
            return FieldDataType.MAP;
        } else if(BOXED_MAP.containsKey(PsiTypesUtil.getPsiClass(type).getQualifiedName())){
            return BOXED_MAP.get(PsiTypesUtil.getPsiClass(type).getQualifiedName());
        } else {
            return FieldDataType.OBJECT;
        }
    }

}
