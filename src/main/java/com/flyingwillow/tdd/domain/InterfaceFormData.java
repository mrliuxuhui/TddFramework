package com.flyingwillow.tdd.domain;

import com.flyingwillow.tdd.resource.InterfaceBundle;
import com.intellij.psi.PsiMethod;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InterfaceFormData {

    private String nameLabel = InterfaceBundle.message("interface.edit.panel.basic.name.label");
    private String name;

    private String pathLabel = InterfaceBundle.message("interface.edit.panel.basic.path.label");
    private String pathValue;
    private String httMethod;

    private String classPathLabel = InterfaceBundle.message("interface.edit.panel.basic.class.label");
    private String classPathValue;
    private String methodName;

    public InterfaceFormData(InterfaceMetaInfo metaInfo) {
        this.name = metaInfo.getName();
        this.pathValue = metaInfo.getId();
        this.httMethod = metaInfo.getHttpMethod().name();
        this.classPathValue = metaInfo.getPsiClass().getQualifiedName();
        this.methodName = ((PsiMethod) metaInfo.getTarget()).getName();
    }
}
