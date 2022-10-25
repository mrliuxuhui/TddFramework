package com.flyingwillow.tdd.editor;

import com.flyingwillow.tdd.domain.InterfaceFormData;
import com.flyingwillow.tdd.provider.InterfaceDataProvider;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.util.ui.FormBuilder;

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

    public InterfaceBaseInfoPanel(InterfaceDataProvider dataProvider){
        this.dataProvider = dataProvider;
        initPanel(this.dataProvider);
        updateData(dataProvider);
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
    public void initPanel(InterfaceDataProvider dataProvider) {
        this.nameLabel = new JLabel();
        this.nameValue = new JTextField();

        this.pathLabel = new JLabel();
        this.pathValue = new JTextField();
        this.httpMethod = new ComboBox();

        this.classPathLabel = new JLabel();
        this.classPath = new JTextField();
        this.classPath.setEnabled(false);
        this.methodName = new JTextField();

        this.setLayout(new BorderLayout());
        JPanel pathWrapper = new JPanel();
        pathWrapper.add(this.httpMethod);
        pathWrapper.add(this.pathValue);
        this.add(pathWrapper);
        JPanel classPathWrapper = new JPanel();
        classPathWrapper.add(this.classPath);
        classPathWrapper.add(new JLabel("."));
        classPathWrapper.add(this.methodName);

        final JPanel panel = FormBuilder.createFormBuilder()
                .addLabeledComponent(nameLabel, nameValue)
                .addSeparator()
                .addLabeledComponent(pathLabel, pathWrapper)
                .addSeparator()
                .addLabeledComponent(classPathLabel, classPathWrapper)
                .getPanel();
        this.add(panel, BorderLayout.CENTER);
    }
}
