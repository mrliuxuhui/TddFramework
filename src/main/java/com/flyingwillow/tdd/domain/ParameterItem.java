package com.flyingwillow.tdd.domain;


import com.flyingwillow.tdd.provider.InterfaceParameterReader;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeElement;
import lombok.Data;

@Data
public class ParameterItem {
    private String name;
    private String comment;
    private String defaultValue;
    private FieldDataType type;
    private boolean required;
    private boolean root = false;

    private boolean hasChildren;
    private String qualifiedClassName;

    private String packageName;
    private String className;
    private PsiElement target;
    private Project project;
    private Module module;
    private InterfaceParameterReader reader;

    public ParameterItem(PsiParameter p, Module module, InterfaceParameterReader parameterReader) {
        init(p, module, parameterReader);
    }

    private void init(PsiElement target, Module module, InterfaceParameterReader parameterReader) {
        this.reader = parameterReader;
        this.target = target;
        this.project = module.getProject();
        this.module = module;
        this.name = this.reader.getName();
        this.comment = this.reader.getComment();
        this.defaultValue = this.reader.getDefaultValue();
        this.type = this.reader.getDataType();
        this.required = this.reader.getRequired();
        this.qualifiedClassName = this.reader.getQualifiedName();
        this.hasChildren = !this.reader.isSimpleType();
        this.className = this.qualifiedClassName.substring(this.qualifiedClassName.lastIndexOf(".")+1);
        this.packageName = this.qualifiedClassName.substring(0, this.qualifiedClassName.lastIndexOf("."));
    }

    private ParameterItem() {
    }

    public ParameterItem(PsiTypeElement returnType, Module module, InterfaceParameterReader parameterReader) {
        init(returnType, module, parameterReader);
    }

    public static ParameterItem createRoot(){
        ParameterItem root = new ParameterItem();
        root.setName("root");
        root.setHasChildren(true);
        root.setRoot(true);
        return root;
    }
}
