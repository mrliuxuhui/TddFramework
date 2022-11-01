package com.flyingwillow.tdd.editor;

import com.flyingwillow.tdd.domain.ParameterType;
import com.flyingwillow.tdd.provider.InterfaceDataProvider;
import com.flyingwillow.tdd.util.ToolkitUtil;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.dualView.TreeTableView;
import com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class InterfaceParameterPanel extends JPanel implements InterfaceEditorPanelInitializer {

    private TreeTableView parameters;

    private ParameterType type;
    private Project project;

    private InterfaceDataProvider dataProvider;

    public InterfaceParameterPanel(InterfaceDataProvider dataProvider, ParameterType type, Project project) {
        this.type = type;
        this.project = project;
        this.dataProvider = dataProvider;
        initPanel(dataProvider);
        updateData(dataProvider);
    }

    @Override
    public void initPanel(InterfaceDataProvider dataProvider) {
        parameters = new TreeTableView(new ListTreeTableModelOnColumns(new DefaultMutableTreeNode(), dataProvider.getColumns()));
        parameters.setAutoCreateColumnsFromModel(true);
        parameters.setRootVisible(false);
        parameters.setCellSelectionEnabled(false);
        parameters.setRowSelectionAllowed(true);
        parameters.setBorder(JBUI.Borders.empty());
        //parameters.setAutoResizeMode(TreeTableView.AUTO_RESIZE_OFF);
        parameters.getTree().setShowsRootHandles(true);
        final ToolbarDecorator decorator = ToolbarDecorator.createDecorator(parameters);
        decorator.setAddAction(anActionButton -> {
                })
                .setRemoveAction(anActionButton -> {
                })
                .setPanelBorder(JBUI.Borders.empty());
        this.setLayout(new BorderLayout());
        this.add(decorator.createPanel(), BorderLayout.CENTER);
    }

    @Override
    public void updateData(InterfaceDataProvider dataProvider) {
        final Future<DefaultMutableTreeNode> future = ToolkitUtil.asyncRead(() -> dataProvider.getParameterRootNode(type), project);

        DumbService.getInstance(project).smartInvokeLater(() -> {
            try {
                ListTreeTableModelOnColumns model = new ListTreeTableModelOnColumns(future.get()
                        , dataProvider.getColumns());
                this.parameters.setModel(model);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void setDataProvider(InterfaceDataProvider dataProvider) {

    }
}
