package com.flyingwillow.tdd.provider;

import com.flyingwillow.tdd.domain.FieldDataType;
import com.flyingwillow.tdd.domain.ParameterItem;
import com.flyingwillow.tdd.util.ToolkitUtil;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiArrayType;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.psi.util.PsiUtil;

import java.util.List;
import java.util.Objects;

public class DefaultPojoReader implements InterfaceParameterReader {

    public static final String API_MODEL = "io.swagger.annotations.ApiModel";
    private PsiType type;

    private PsiParameter parameter;
    private PsiClass targetClass;

    private PsiAnnotation swaggerAnnotation;

    private boolean listable;

    public DefaultPojoReader(PsiType type) {
        this.type = type;
        this.targetClass = PsiTypesUtil.getPsiClass(type);
        this.swaggerAnnotation = this.targetClass.getAnnotation(API_MODEL);
    }

    public DefaultPojoReader(PsiParameter parameter) {
        this(parameter.getType());
        this.parameter = parameter;
    }

    public DefaultPojoReader(PsiParameter parameter, boolean listable) {
        this.parameter = parameter;
        this.listable = listable;
        if (ToolkitUtil.isArrayType(parameter.getType())) {
            this.type = ((PsiArrayType) parameter.getType()).getComponentType();
        } else if (ToolkitUtil.isListableType(parameter.getType())) {
            this.type = PsiUtil.extractIterableTypeParameter(parameter.getType(), false);
        }
        if (Objects.nonNull(this.type)) {
            this.targetClass = PsiTypesUtil.getPsiClass(this.type);
            this.swaggerAnnotation = this.targetClass.getAnnotation(API_MODEL);
        }

    }

    @Override
    public String getComment() {
        if (Objects.nonNull(swaggerAnnotation) && Objects.nonNull(swaggerAnnotation.findAttributeValue("value"))) {
            return ToolkitUtil.parseStringValue(swaggerAnnotation.findAttributeValue("value"));
        } else {
            return this.targetClass.getName();
        }
    }

    @Override
    public String getName() {
        StringBuilder sb = new StringBuilder();
        if (Objects.nonNull(parameter)) {
            sb.append(parameter.getName());
        } else {
            sb.append(this.targetClass.getName());
        }
        if (listable) {
            sb.append("[]");
        }
        return sb.toString();
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
        if (listable) {
            return ToolkitUtil.getDataType(this.type);
        } else {
            return FieldDataType.OBJECT;
        }
    }

    @Override
    public String getQualifiedName() {
        return this.targetClass.getQualifiedName();
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
