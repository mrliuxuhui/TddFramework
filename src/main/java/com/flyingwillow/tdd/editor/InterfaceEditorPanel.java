package com.flyingwillow.tdd.editor;

import com.flyingwillow.tdd.provider.InterfaceDataProvider;
import com.intellij.ui.components.JBTabbedPane;

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
    private JTabbedPane tabbedPanel;
    private InterfaceParameterPanel inputParams;
    private InterfaceParameterPanel outputParams;

    private InterfaceDataProvider dataProvider;

    public InterfaceEditorPanel(){
        initPanel();
    }

    @Override
    public void setDataProvider(InterfaceDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public void updateData(InterfaceDataProvider dataProvider) {
        setDataProvider(dataProvider);
        this.baseInfoPanel.updateData(dataProvider);
    }

    @Override
    public void initPanel() {
        this.setLayout(new FlowLayout());
        this.baseInfoPanel = new InterfaceBaseInfoPanel();
        this.inputParams = new InterfaceParameterPanel();
        this.outputParams = new InterfaceParameterPanel();
        this.add(this.baseInfoPanel);
        this.tabbedPanel = new JBTabbedPane();
        this.tabbedPanel.add("入参配置",this.inputParams);
        this.tabbedPanel.add("出参配置",this.outputParams);
        this.add(tabbedPanel);
    }
}
