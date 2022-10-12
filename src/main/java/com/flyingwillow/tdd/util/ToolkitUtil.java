package com.flyingwillow.tdd.util;

import com.flyingwillow.tdd.domain.InterfaceMetaInfo;
import com.flyingwillow.tdd.service.ComponentSearchService;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiArrayInitializerMemberValue;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.impl.source.tree.java.PsiArrayInitializerMemberValueImpl;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.tree.TreeUtil;

import javax.swing.tree.TreePath;
import java.util.Objects;

public class ToolkitUtil {

    private ToolkitUtil() {

    }

    public static String parseStringValue(PsiAnnotationMemberValue value) {
        if (value instanceof PsiLiteralExpression) {
            return (String) ((PsiLiteralExpression) value).getValue();
        } else if (value instanceof PsiArrayInitializerMemberValue) {
            return (String) ((PsiLiteralExpression) ((PsiArrayInitializerMemberValueImpl) value).getInitializers()[0]).getValue();
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

}
