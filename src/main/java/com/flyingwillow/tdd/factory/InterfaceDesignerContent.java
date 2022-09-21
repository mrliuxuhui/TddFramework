package com.flyingwillow.tdd.factory;

import com.flyingwillow.tdd.domain.InterfaceMetaInfo;
import com.flyingwillow.tdd.domain.InterfaceMetaType;
import com.flyingwillow.tdd.service.InterfaceMetaService;
import com.intellij.core.CoreBundle;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Constraints;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.ex.ComboBoxAction;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.packageDependencies.ui.TreeModel;
import com.intellij.ui.treeStructure.Tree;
import icons.JavaUltimateIcons;
import org.apache.commons.collections.CollectionUtils;
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
        final List<Module> moduleList = interfaceMetaService.getValidModules(project);
        if (CollectionUtils.isEmpty(moduleList)) {
            interfaceTree.getEmptyText().setText(interfaceMetaService.getErrorMsg(-1));
        } else {
            loadingTree(moduleList.get(0));
            // drag and drop config
            interfaceTree.setTransferHandler(new InterfaceTreeTransferHandler(interfaceTree));
            interfaceTree.setDragEnabled(true);
            interfaceTree.setDropMode(DropMode.ON);
        }
    }

    private void loadingTree(Module module) {
        interfaceTree.setPaintBusy(true);
        final List<InterfaceMetaInfo> list = interfaceMetaService.selectAll(project);

        final TreeModel model = InterfaceMetaInfo.buildTree(list, project);
        interfaceTree.setModel(model);
        InterfaceCellRender render = new InterfaceCellRender();
        render.setLeafIcon(JavaUltimateIcons.Web.RequestMapping);
        render.setOpenIcon(AllIcons.Nodes.WebFolder);
        render.setClosedIcon(AllIcons.Nodes.WebFolder);
        interfaceTree.setCellRenderer(render);
        interfaceTree.setRowHeight(20);
        interfaceTree.setPaintBusy(false);
    }

    private void reloadTree(Module module) {
        loadingTree(module);
        interfaceTree.validate();
    }

    private void createMainPanel() {

        this.mainPanel = new SimpleToolWindowPanel(true, true);
        final DefaultActionGroup group = (DefaultActionGroup) ActionManager.getInstance().getAction("interface-toolbar");

        final List<Module> modules = interfaceMetaService.getValidModules(project);
        if (CollectionUtils.isNotEmpty(modules)) {
            final ComboBoxAction comboBoxAction = new DropdownComboboxAction(modules.get(0), modules);
            group.add(comboBoxAction, Constraints.FIRST);
        }

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
                createActionButton(Arrays.asList("interface.popup.class.add", "interface.popup.package.del")).forEach(buttonGroup::add);
            } else if (metaInfo.getType() == InterfaceMetaType.CONTROLLER) {
                createActionButton(Arrays.asList("interface.popup.method.add", "interface.popup.class.del")).forEach(buttonGroup::add);
            } else if (metaInfo.getType() == InterfaceMetaType.INTERFACE) {
                createActionButton(Arrays.asList("interface.popup.method.edit"
                        , "interface.popup.method.edit.test"
                        , "interface.popup.method.del")).forEach(buttonGroup::add);
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

    public class DropdownOptionAction extends AnAction {

        private Module module;

        private DropdownComboboxAction dropdownCombobox;

        public DropdownOptionAction(Module module, DropdownComboboxAction dropdownCombobox) {
            this.module = module;
            this.dropdownCombobox = dropdownCombobox;
            final Presentation presentation = getTemplatePresentation();
            presentation.setIcon(AllIcons.Nodes.Module);
            presentation.setText(module.getName());
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            this.dropdownCombobox.setSelectedItem(this.module);
            reloadTree(this.module);
        }
    }

    public class DropdownComboboxAction extends ComboBoxAction {

        private Module selectedModule;
        private List<Module> modules;

        public DropdownComboboxAction(Module selectedModule, List<Module> modules) {
            this.selectedModule = selectedModule;
            this.modules = modules;
        }

        @Override
        protected @NotNull DefaultActionGroup createPopupActionGroup(JComponent button) {
            DefaultActionGroup dropdownGroup = new DefaultActionGroup();
            final List<DropdownOptionAction> optionActions = modules.stream().map(m -> new DropdownOptionAction(m, this)).collect(Collectors.toList());
            dropdownGroup.addAll(optionActions);
            return dropdownGroup;
        }

        public void setSelectedItem(Module module) {
            this.selectedModule = module;
        }

        @Override
        public void update(@NotNull AnActionEvent e) {
            super.update(e);
            final Presentation presentation = e.getPresentation();
            if (selectedModule == null) {
                presentation.setIcon(AllIcons.FileTypes.Unknown);
                presentation.setText(CoreBundle.message("filetype.unknown.description"));
            } else {
                presentation.setIcon(AllIcons.Nodes.Module);
                presentation.setText(selectedModule.getName());
            }
        }
    }
}
