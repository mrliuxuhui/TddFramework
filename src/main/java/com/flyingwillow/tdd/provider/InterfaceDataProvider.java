package com.flyingwillow.tdd.provider;

import com.flyingwillow.tdd.domain.InterfaceFormData;
import com.flyingwillow.tdd.domain.ParameterType;
import com.intellij.ui.treeStructure.treetable.TreeTableCellRenderer;
import com.intellij.ui.treeStructure.treetable.TreeTableModel;
import com.intellij.util.ui.ColumnInfo;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public interface InterfaceDataProvider {
    InterfaceFormData getFormData();

    ComboBoxModel<String> getHttpMethodModel();

    String getInputParamTitle();

    String getOutputParamTitle();

    DefaultMutableTreeNode getParameterRootNode(ParameterType type);

    ColumnInfo[] getColumns();
}
