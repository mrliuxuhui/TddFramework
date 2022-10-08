package com.flyingwillow.tdd.domain;

import com.flyingwillow.tdd.service.InterfaceNameReader;
import com.intellij.packageDependencies.ui.TreeModel;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class InterfaceMetaInfo {

    private String id;
    private String name;
    private String parentId;
    private Integer level;
    private Boolean leaf = true;
    private InterfaceMetaType type;

    private PsiElement target;

    public InterfaceMetaInfo() {
    }

    public InterfaceMetaInfo(String id, String name, String parentId, Integer level, Boolean leaf, InterfaceMetaType type) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.level = level;
        this.leaf = leaf;
        this.type = type;
    }

    public InterfaceMetaInfo(PsiJavaFile javaFile, InterfaceNameReader reader, PsiClass cls) {
        this.id = reader.getControllerId(cls);
        this.type = InterfaceMetaType.CONTROLLER;
        this.leaf = false;
        this.level = 2;
        this.parentId = reader.getPackageId(javaFile);
        this.name = reader.getControllerName(cls);
        this.target = cls;
    }

    public InterfaceMetaInfo(PsiMethod method, PsiClass javaClass, InterfaceNameReader reader) {
        this.id = reader.getInterfaceId(method, javaClass);
        this.type = InterfaceMetaType.INTERFACE;
        this.leaf = false;
        this.level = 3;
        this.parentId = reader.getControllerId(javaClass);
        this.name = reader.getInterfaceName(method, javaClass);
        this.target = method;
    }

    public InterfaceMetaInfo(String packageName, List<PsiJavaFile> javaFiles, InterfaceNameReader reader) {
        this.id = CollectionUtils.isNotEmpty(javaFiles) ? reader.getPackageId(javaFiles.get(0)) : packageName;
        this.type = InterfaceMetaType.PACKAGE;
        this.leaf = false;
        this.level = 1;
        this.parentId = null;
        this.name = CollectionUtils.isNotEmpty(javaFiles) ? reader.getPackageName(javaFiles.get(0)) : packageName;
        if (this.name.contains(".")) {
            this.name = this.name.substring(this.name.lastIndexOf(".") + 1);
        }
        this.target = null;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static TreeModel buildTree(List<InterfaceMetaInfo> list, com.intellij.openapi.module.Module module) {
        if (CollectionUtils.isEmpty(list)) {
            return new TreeModel(null);
        }
        InterfaceMetaInfo rootInfo = new InterfaceMetaInfo();
        rootInfo.setName(module.getName());
        rootInfo.setParentId("0");
        rootInfo.setLevel(0);
        rootInfo.setType(InterfaceMetaType.ROOT);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootInfo);
        final List<DefaultMutableTreeNode> nodeList = list.stream()
                .map(e -> new DefaultMutableTreeNode(e, !e.leaf)).collect(Collectors.toList());
        final Map<String, DefaultMutableTreeNode> infoMap = nodeList.stream()
                .collect(Collectors.toMap(e -> String.valueOf(((InterfaceMetaInfo) e.getUserObject()).getId()), Function.identity()));

        nodeList.forEach(node -> {
            final InterfaceMetaInfo meta = (InterfaceMetaInfo) node.getUserObject();
            final Integer level = meta.getLevel();
            if (1 == level) {
                root.add(node);
                node.setParent(root);
            } else {
                final DefaultMutableTreeNode parent = infoMap.get(String.valueOf(meta.getParentId()));
                if (!parent.getAllowsChildren()) {
                    parent.setAllowsChildren(true);
                }
                parent.add(node);
                node.setParent(parent);
            }
        });

        return new TreeModel(root);
    }
}
