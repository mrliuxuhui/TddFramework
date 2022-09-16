package com.flyingwillow.tdd.factory;

import com.flyingwillow.tdd.domain.InterfaceMetaInfo;
import com.flyingwillow.tdd.domain.InterfaceMetaType;
import com.flyingwillow.tdd.service.InterfaceMetaService;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.packageDependencies.ui.TreeModel;
import com.intellij.ui.treeStructure.Tree;
import icons.JavaUltimateIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InterfaceDesignerContent {

    private SimpleToolWindowPanel mainPanel;
    private Tree interfaceTree;

    private Project project;
    private ToolWindow toolWindow;
    private InterfaceMetaService interfaceMetaService;

    public InterfaceDesignerContent(Project project, ToolWindow toolWindow) {
        this.project = project;
        this.toolWindow = toolWindow;
        // 加载数据
        this.interfaceMetaService = project.getService(InterfaceMetaService.class);

        createUIComponents();
    }

    private void createUIComponents() {

        // create main panel
        createMainPanel();

        // create Tree
        createTree();
    }

    private void createTree() {
        this.interfaceTree = new Tree();
        this.mainPanel.setContent(this.interfaceTree);
        //
        interfaceTree.setPaintBusy(true);
        loadingTree();
        interfaceTree.setPaintBusy(false);

        // drag listener

    }

    private void loadingTree() {
        final List<InterfaceMetaInfo> list = interfaceMetaService.selectAll(project);
        final TreeModel model = InterfaceMetaInfo.buildTree(list, project);
        interfaceTree.setModel(model);
        InterfaceCellRender render = new InterfaceCellRender();
        render.setLeafIcon(JavaUltimateIcons.Web.RequestMapping);
        render.setOpenIcon(AllIcons.Nodes.WebFolder);
        render.setClosedIcon(AllIcons.Nodes.WebFolder);
        interfaceTree.setCellRenderer(render);
        interfaceTree.setRowHeight(20);
        //
        interfaceTree.setDragEnabled(true);
    }

    private void createMainPanel() {

        this.mainPanel = new SimpleToolWindowPanel(true, true);
        final ActionGroup group = (ActionGroup) ActionManager.getInstance().getAction("interface-toolbar");
        final ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLWINDOW_TOOLBAR_BAR, group, true);
        toolbar.setTargetComponent(mainPanel);
        mainPanel.setToolbar(toolbar.getComponent());
    }

    public SimpleToolWindowPanel getMainPanel() {
        return mainPanel;
    }

    public Tree getInterfaceTree() {
        return interfaceTree;
    }

    public ToolWindow getToolWindow() {
        return toolWindow;
    }

    public class InterfaceCellRender extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            // install actions
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            final InterfaceMetaInfo metaInfo = (InterfaceMetaInfo) node.getUserObject();
            this.setText(metaInfo.getName());
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(1, 2));
            panel.setOpaque(false);
            panel.setBackground(UIManager.getColor("Tree.textBackground"));
            panel.add(super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus));

            JPanel buttonGroup = new JPanel();
            buttonGroup.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 1));
            buttonGroup.setOpaque(false);
            buttonGroup.setBackground(UIManager.getColor("Tree.textBackground"));
            panel.add(buttonGroup);

            if (metaInfo.getType() == InterfaceMetaType.PACKAGE) {
                createActionButton(Arrays.asList("interface.popup.class.add", "interface.popup.package.del")).forEach(com -> buttonGroup.add(com));
            } else if (metaInfo.getType() == InterfaceMetaType.CONTROLLER) {
                createActionButton(Arrays.asList("interface.popup.method.add", "interface.popup.class.del")).forEach(com -> buttonGroup.add(com));
            } else if (metaInfo.getType() == InterfaceMetaType.INTERFACE) {
                createActionButton(Arrays.asList("interface.popup.method.del")).forEach(com -> buttonGroup.add(com));
            } else {
                return this;
            }
            return panel;
        }

        @NotNull
        private List<ActionButton> createActionButton(List<String> actionIds) {
            ActionManager manager = ActionManager.getInstance();
            return actionIds.stream().filter(id -> Objects.nonNull(manager.getAction(id)))
                    .map(id -> {
                        final AnAction addAction = manager.getAction(id);
                        final Presentation presentation = addAction.getTemplatePresentation();
                        ActionButton addBtn = new ActionButton(addAction, presentation, ActionPlaces.UNKNOWN, ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE);
                        presentation.putClientProperty("SWING_BUTTON_KEY", addBtn);
                        return addBtn;
                    }).collect(Collectors.toList());
        }
    }
}
