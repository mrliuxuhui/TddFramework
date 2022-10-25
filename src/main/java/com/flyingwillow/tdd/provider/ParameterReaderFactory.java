package com.flyingwillow.tdd.provider;

import com.flyingwillow.tdd.util.ToolkitUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;

public class ParameterReaderFactory {

    public static final ParameterReaderFactory INSTANCE = new ParameterReaderFactory();

    private ParameterReaderFactory(){

    }

    public InterfaceParameterReader getParameterReader(PsiType type){
        return new DefaultPojoReader();
    }
    public InterfaceParameterReader getParameterReader(PsiParameter parameter){
        if (ToolkitUtil.isSimpleType(parameter.getType())){
            return new DefaultSimpleParamReader(parameter);
        } else if(ToolkitUtil.isListableType(parameter.getType())){
            return new DefaultPojoReader();
        } else {
            return new DefaultPojoReader();
        }
    }

    public InterfaceParameterReader getFieldReader(PsiClass psiClass, PsiField field){
        return new DefaultPojoFieldReader();
    }
}
