package com.flyingwillow.tdd.editor;

import com.flyingwillow.tdd.domain.InterfaceFormData;
import com.flyingwillow.tdd.provider.InterfaceDataProvider;
import com.intellij.openapi.ui.ComboBox;

import javax.swing.*;
import java.awt.*;

public class InterfaceBaseInfoPanel extends JPanel implements InterfaceEditorPanelInitializer{

    /**
     *  接口名
     * */
    private JLabel nameLabel;
    private JTextField nameValue;

    /**
     *  接口路径
     * */
    private JLabel pathLabel;
    private JTextField pathValue;
    private JComboBox<String> httpMethod;

    /**
     *  接口方法位置
     * */
    private JLabel classPathLabel;
    private JTextField classPath;
    private JTextField methodName;

    private InterfaceDataProvider dataProvider;

    public InterfaceBaseInfoPanel(){
        initPanel();
    }

    @Override
    public void setDataProvider(InterfaceDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public void updateData(InterfaceDataProvider provider) {
        setDataProvider(provider);
        final InterfaceFormData formData = provider.getFormData();
        this.nameLabel.setText(formData.getNameLabel());
        this.nameValue.setText(formData.getName());
        this.pathLabel.setText(formData.getPathLabel());
        this.pathValue.setText(formData.getPathValue());
        this.httpMethod.setModel(provider.getHttpMethodModel());
        this.httpMethod.setSelectedItem(formData.getHttMethod());
        this.classPathLabel.setText(formData.getClassPathLabel());
        this.classPath.setText(formData.getClassPathValue());
        this.methodName.setText(formData.getMethodName());
    }

    @Override
    public void initPanel() {
        this.nameLabel = new JLabel();
        this.nameValue = new JTextField();

        this.pathLabel = new JLabel();
        this.pathValue = new JTextField();
        this.httpMethod = new ComboBox();

        this.classPathLabel = new JLabel();
        this.classPath = new JTextField();
        this.classPath.setEnabled(false);
        this.methodName = new JTextField();

        this.setLayout(new GridLayout(3,2, 5, 5));
        this.add(nameLabel);
        this.add(nameValue);
        this.add(pathLabel);
        JPanel pathWrapper = new JPanel();
        pathWrapper.add(this.httpMethod);
        pathWrapper.add(this.pathValue);
        this.add(pathWrapper);
        this.add(this.classPathLabel);
        JPanel classPathWrapper = new JPanel();
        classPathWrapper.add(this.classPath);
        classPathWrapper.add(this.methodName);
        this.add(classPathWrapper);
    }
}
