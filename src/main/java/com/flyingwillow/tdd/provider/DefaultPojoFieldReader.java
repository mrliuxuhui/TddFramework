package com.flyingwillow.tdd.provider;

import com.flyingwillow.tdd.domain.FieldDataType;
import com.flyingwillow.tdd.domain.ParameterItem;
import com.flyingwillow.tdd.util.ToolkitUtil;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTypesUtil;

import java.util.List;
import java.util.Objects;

public class DefaultPojoFieldReader implements InterfaceParameterReader {

    public static final String API_MODEL_PROPERTY = "io.swagger.annotations.ApiModelProperty";
    private PsiClass parentClass;
    private PsiField psiField;
    private PsiType fieldType;
    private PsiClass fieldClass;
    private PsiAnnotation swaggerAnnotation;

    public DefaultPojoFieldReader(PsiClass psiClass, PsiField field) {
        this.psiField = field;
        this.parentClass = psiClass;
        this.fieldType = field.getType();
        this.fieldClass = PsiTypesUtil.getPsiClass(fieldType);
        this.swaggerAnnotation = this.psiField.getAnnotation(API_MODEL_PROPERTY);
    }

    @Override
    public String getComment() {
        if(Objects.nonNull(swaggerAnnotation) && Objects.nonNull(swaggerAnnotation.findAttributeValue("value"))){
            return ToolkitUtil.parseStringValue(swaggerAnnotation.findAttributeValue("value"));
        } else {
            return this.psiField.getName();
        }
    }

    @Override
    public String getName() {
        if(Objects.nonNull(swaggerAnnotation) && Objects.nonNull(swaggerAnnotation.findAttributeValue("value"))){
            return ToolkitUtil.parseStringValue(swaggerAnnotation.findAttributeValue("value"));
        } else {
            return this.psiField.getName();
        }
    }

    @Override
    public Boolean getRequired() {
        if(Objects.nonNull(swaggerAnnotation) && Objects.nonNull(swaggerAnnotation.findAttributeValue("required"))){
            return Boolean.valueOf(ToolkitUtil.parseStringValue(swaggerAnnotation.findAttributeValue("required")));
        } else {
            return false;
        }
    }

    @Override
    public String getDefaultValue() {
        if(Objects.nonNull(swaggerAnnotation) && Objects.nonNull(swaggerAnnotation.findAttributeValue("defaultValue"))){
            return ToolkitUtil.parseStringValue(swaggerAnnotation.findAttributeValue("defaultValue"));
        } else {
            return "";
        }
    }

    @Override
    public FieldDataType getDataType() {
        return ToolkitUtil.getDataType(this.fieldType);
    }

    @Override
    public String getQualifiedName() {
        return this.fieldClass.getQualifiedName();
    }

    @Override
    public List<ParameterItem> loadChildren() {
        return null;
    }

    @Override
    public boolean isSimpleType() {
        final FieldDataType dataType = getDataType();
        return dataType != FieldDataType.OBJECT
                && dataType != FieldDataType.LIST
                && dataType != FieldDataType.MAP;
    }
}
