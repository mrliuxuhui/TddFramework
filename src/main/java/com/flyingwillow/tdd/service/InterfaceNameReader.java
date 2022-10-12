package com.flyingwillow.tdd.service;

import com.flyingwillow.tdd.util.HttpMethodEnum;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;

public interface InterfaceNameReader {

    static final InterfaceNameReader INSTANCE = new DefaultInterfaceNameReader();

    String getPackageName(PsiJavaFile file);

    String getPackageId(PsiJavaFile file);

    String getControllerName(PsiClass file);

    String getControllerId(PsiClass file);

    String getInterfaceName(PsiMethod method, PsiClass file);

    String getInterfaceId(PsiMethod method, PsiClass file);

    boolean isValidInterface(PsiMethod method);

    PsiClass getMainClass(PsiJavaFile javaFile);

    String defaultPackage(String packagePath);

    HttpMethodEnum getHttpMethod(PsiMethod method);
}
