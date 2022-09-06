package com.flyingwillow.tdd.factory;

import com.flyingwillow.tdd.domain.InterfaceMetaInfo;
import com.flyingwillow.tdd.service.InterfaceMetaService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.packageDependencies.ui.TreeModel;
import com.intellij.ui.treeStructure.Tree;

import javax.swing.*;
import java.util.List;

public class InterfaceDesigner {
    private JPanel mainPanel;
    private JScrollPane container;
    private Tree interfaceTree;

    private Project project;
    private ToolWindow toolWindow;
    private InterfaceMetaService interfaceMetaService;

    public InterfaceDesigner(Project project, ToolWindow toolWindow) {
        this.project = project;
        this.toolWindow = toolWindow;

        this.interfaceMetaService = project.getService(InterfaceMetaService.class);

        // 加载数据
        interfaceTree.setPaintBusy(true);
        loadingTree();
        interfaceTree.setPaintBusy(false);

    }

    private void loadingTree() {
        final List<InterfaceMetaInfo> list = interfaceMetaService.selectAll(project);
        final TreeModel model = InterfaceMetaInfo.buildTree(list, project);
        interfaceTree.setModel(model);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
