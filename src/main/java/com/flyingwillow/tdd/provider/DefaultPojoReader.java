package com.flyingwillow.tdd.provider;

import com.flyingwillow.tdd.domain.FieldDataType;
import com.flyingwillow.tdd.domain.ParameterItem;
import com.intellij.psi.PsiElement;

import java.util.List;

public class DefaultPojoReader implements InterfaceParameterReader{
    @Override
    public String getComment() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Boolean getRequired() {
        return true;
    }

    @Override
    public String getDefaultValue() {
        return null;
    }

    @Override
    public FieldDataType getDataType() {
        return null;
    }

    @Override
    public String getQualifiedName() {
        return null;
    }

    @Override
    public List<ParameterItem> loadChildren() {
        return null;
    }

    @Override
    public boolean isSimpleType() {
        return false;
    }
}
