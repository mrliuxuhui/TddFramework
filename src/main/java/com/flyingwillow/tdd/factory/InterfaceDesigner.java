package com.flyingwillow.tdd.factory;

import com.flyingwillow.tdd.service.InterfaceMetaService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

public class InterfaceDesigner {
    private JPanel mainPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JComboBox comboBox1;
    private JTabbedPane tabbedPane1;
    private JTable table1;

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
