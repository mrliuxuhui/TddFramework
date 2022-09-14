package com.flyingwillow.tdd.factory;

import com.flyingwillow.tdd.domain.InterfaceMetaInfo;
import com.flyingwillow.tdd.service.InterfaceMetaService;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.packageDependencies.ui.TreeModel;
import com.intellij.ui.treeStructure.Tree;
import icons.JavaUltimateIcons;

import javax.swing.tree.DefaultTreeCellRenderer;
import java.util.List;

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
        DefaultTreeCellRenderer render = new DefaultTreeCellRenderer();
        render.setLeafIcon(JavaUltimateIcons.Web.RequestMapping);
        render.setOpenIcon(AllIcons.Nodes.WebFolder);
        render.setClosedIcon(AllIcons.Nodes.WebFolder);
        interfaceTree.setCellRenderer(render);
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
}
