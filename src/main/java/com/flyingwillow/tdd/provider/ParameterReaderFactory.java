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
        return new DefaultPojoReader(type);
    }
    public InterfaceParameterReader getParameterReader(PsiParameter parameter){
        if (ToolkitUtil.isSimpleType(parameter.getType())){
            return new DefaultSimpleParamReader(parameter);
        } else if(ToolkitUtil.isListableType(parameter.getType())){
            return new DefaultPojoReader(parameter, true);
        } else {
            return new DefaultPojoReader(parameter);
        }
    }

    public InterfaceParameterReader getFieldReader(PsiClass psiClass, PsiField field){
        return new DefaultPojoFieldReader(psiClass, field);
    }
}
