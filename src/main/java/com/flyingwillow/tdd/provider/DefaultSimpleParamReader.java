package com.flyingwillow.tdd.provider;

import com.flyingwillow.tdd.domain.FieldDataType;
import com.flyingwillow.tdd.domain.ParameterItem;
import com.flyingwillow.tdd.util.ToolkitUtil;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.util.PsiTypesUtil;

import java.util.List;
import java.util.Objects;

public class DefaultSimpleParamReader implements InterfaceParameterReader {

    private PsiParameter parameter;
    private PsiAnnotation paramAnnotation;

    private PsiAnnotation swaggerAnnotation;

    public DefaultSimpleParamReader(PsiParameter parameter) {
        this.parameter = parameter;
        this.paramAnnotation = parameter.getAnnotation(REQUEST_PARAM);
        this.swaggerAnnotation = parameter.getAnnotation(SWAGGER_PARAM_ANNOTATION);
    }

    @Override
    public String getComment() {
        if (Objects.nonNull(swaggerAnnotation) && Objects.nonNull(swaggerAnnotation.findAttributeValue("value"))) {
            return ToolkitUtil.parseStringValue(swaggerAnnotation.findAttributeValue("value"));
        } else if (Objects.nonNull(paramAnnotation) && Objects.nonNull(paramAnnotation.findAttributeValue("name"))) {
            return ToolkitUtil.parseStringValue(paramAnnotation.findAttributeValue("name"));
        } else {
            return parameter.getName();
        }
    }

    @Override
    public String getName() {
        return parameter.getName();
    }

    @Override
    public Boolean getRequired() {
        if(Objects.nonNull(paramAnnotation) && Objects.nonNull(paramAnnotation.findAttributeValue("required"))){
            return Boolean.valueOf(ToolkitUtil.parseStringValue(paramAnnotation.findAttributeValue("required")));
        }
        return true;
    }

    @Override
    public String getDefaultValue() {
        if(Objects.nonNull(paramAnnotation) && Objects.nonNull(paramAnnotation.findAttributeValue("defaultValue"))){
            return ToolkitUtil.parseStringValue(paramAnnotation.findAttributeValue("defaultValue"));
        }else if(Objects.nonNull(swaggerAnnotation) && Objects.nonNull(swaggerAnnotation.findAttributeValue("defaultValue"))){
            return ToolkitUtil.parseStringValue(swaggerAnnotation.findAttributeValue("defaultValue"));
        } else {
            return "";
        }
    }

    @Override
    public FieldDataType getDataType() {
        return ToolkitUtil.getDataType(parameter.getType());
    }

    @Override
    public String getQualifiedName() {
        return PsiTypesUtil.getPsiClass(parameter.getType()).getQualifiedName();
    }

    @Override
    public List<ParameterItem> loadChildren() {
        return null;
    }

    @Override
    public boolean isSimpleType() {
        return true;
    }
}
