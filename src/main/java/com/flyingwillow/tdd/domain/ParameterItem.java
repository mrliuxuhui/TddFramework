package com.flyingwillow.tdd.domain;


import com.flyingwillow.tdd.provider.InterfaceParameterReader;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeElement;
import lombok.Data;

@Data
public class ParameterItem {

    public static final String[] FIELD_TITLES = {"字段名","字段类型", "字段默认值", "字段描述", "是否必须", "类名"};
    private String name;
    private String comment;
    private String defaultValue;
    private FieldDataType type;
    private boolean required;

    private boolean hasChildren;
    private String qualifiedClassName;
    private PsiElement target;
    private Project project;
    private InterfaceParameterReader reader;

    public ParameterItem(PsiParameter p, Project project, InterfaceParameterReader parameterReader) {
        this.reader = parameterReader;
        this.target = p;
        this.project = project;
        this.name = this.reader.getName();
        this.comment = this.reader.getComment();
        this.defaultValue = this.reader.getDefaultValue();
        this.type = this.reader.getDataType();
        this.required = this.reader.getRequired();
        this.qualifiedClassName = this.reader.getQualifiedName();
        this.hasChildren = this.reader.isSimpleType();
    }

    private ParameterItem() {
    }

    public ParameterItem(PsiTypeElement returnType, Project project, InterfaceParameterReader parameterReader) {
        this.reader = parameterReader;
        this.target = returnType;
        this.project = project;
        this.name = this.reader.getName();
        this.comment = this.reader.getComment();
        this.defaultValue = this.reader.getDefaultValue();
        this.type = this.reader.getDataType();
        this.required = this.reader.getRequired();
        this.qualifiedClassName = this.reader.getQualifiedName();
        this.hasChildren = this.reader.isSimpleType();
    }

    public static ParameterItem createRoot(){
        ParameterItem root = new ParameterItem();
        root.setName("root");
        root.setHasChildren(true);
        return root;
    }
}
