package com.flyingwillow.tdd.factory;

import com.flyingwillow.tdd.service.InterfaceMetaService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.treeStructure.Tree;

import javax.swing.*;

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

        //
        createUIComponents();

    }

    private void loadingTree() {

    }

    private void createUIComponents() {
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
