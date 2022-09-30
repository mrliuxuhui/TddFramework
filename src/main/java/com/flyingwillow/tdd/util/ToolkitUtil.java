package com.flyingwillow.tdd.util;

import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiArrayInitializerMemberValue;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.impl.source.tree.java.PsiArrayInitializerMemberValueImpl;

public class ToolkitUtil {

    private ToolkitUtil(){

    }

    public static String parseStringValue(PsiAnnotationMemberValue value){
        if(value instanceof PsiLiteralExpression){
            return (String) ((PsiLiteralExpression) value).getValue();
        } else if(value instanceof PsiArrayInitializerMemberValue){
            return (String) ((PsiLiteralExpression)((PsiArrayInitializerMemberValueImpl) value).getInitializers()[0]).getValue();
        } else {
            return value.getText();
        }
    }

}
