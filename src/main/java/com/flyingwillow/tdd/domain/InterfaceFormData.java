package com.flyingwillow.tdd.domain;

import com.intellij.psi.PsiMethod;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InterfaceFormData {

    private String nameLabel = "接口名";
    private String name;

    private String pathLabel = "接口路径";
    private String pathValue;
    private String httMethod;

    private String classPathLabel = "类路径";
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
