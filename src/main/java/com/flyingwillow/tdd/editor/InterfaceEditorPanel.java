package com.flyingwillow.tdd.editor;

import com.flyingwillow.tdd.domain.ParameterType;
import com.flyingwillow.tdd.provider.InterfaceDataProvider;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

public class InterfaceEditorPanel extends JPanel implements InterfaceEditorPanelInitializer{

    /**
     *  基本信息
     * */
    private InterfaceBaseInfoPanel baseInfoPanel;

    /**
     *  入参出参
     * */
    private JBTabbedPane tabbedPanel;
    private InterfaceParameterPanel inputParams;
    private InterfaceParameterPanel outputParams;

    private InterfaceDataProvider dataProvider;

    private Project project;

    public InterfaceEditorPanel(Project project, InterfaceDataProvider dataProvider){
        this.project = project;
        this.dataProvider = dataProvider;
        initPanel(dataProvider);
    }

    @Override
    public void setDataProvider(InterfaceDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public void updateData(InterfaceDataProvider dataProvider) {
        setDataProvider(dataProvider);
        this.baseInfoPanel.updateData(dataProvider);
        this.inputParams.updateData(dataProvider);
    }

    @Override
    public void initPanel(InterfaceDataProvider dataProvider) {
        this.setLayout(new GridLayout(2, 1));
        this.setBorder(BorderFactory.createEmptyBorder(10,20,20,10));
        this.baseInfoPanel = new InterfaceBaseInfoPanel(dataProvider);
        this.inputParams = new InterfaceParameterPanel(dataProvider, ParameterType.INPUT, project);
        this.outputParams = new InterfaceParameterPanel(dataProvider, ParameterType.OUTPUT, project);
        this.add(this.baseInfoPanel);
        this.tabbedPanel = new JBTabbedPane(SwingConstants.TOP);
        tabbedPanel.setBorder(BorderFactory.createLineBorder(new JBColor(0xC9C9C9, 0x2C2F30)));
        tabbedPanel.setTabComponentInsets(JBUI.insets(0));
        this.tabbedPanel.add("入参配置",this.inputParams);
        this.tabbedPanel.add("出参配置",this.outputParams);
        this.add(tabbedPanel);
    }
}
