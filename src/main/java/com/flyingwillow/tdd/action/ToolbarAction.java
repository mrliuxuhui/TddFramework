package com.flyingwillow.tdd.action;

import com.flyingwillow.tdd.service.ComponentSearchService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class ToolbarAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final ComponentSearchService service = e.getProject().getService(ComponentSearchService.class);
        final Tree interfaceTree = service.getContent().getInterfaceTree();
        final String text = e.getPresentation().getText();
        if (text.equals("close-all")) {
            closeAll((DefaultMutableTreeNode) interfaceTree.getModel().getRoot(), interfaceTree);
        } else if (text.equals("expand-all")) {
            expandAll((DefaultMutableTreeNode) interfaceTree.getModel().getRoot(), interfaceTree);
        }
    }

    private void expandAll(DefaultMutableTreeNode root, Tree tree) {
        getChildByOpenStatus(false, root, tree).forEach(node -> tree.expandPath(new TreePath(node.getPath())));
    }

    private void closeAll(DefaultMutableTreeNode root, Tree tree) {
        getChildByOpenStatus(true, root, tree).forEach(node -> tree.collapsePath(new TreePath(node.getPath())));
    }

    private List<DefaultMutableTreeNode> getChildByOpenStatus(boolean opened, DefaultMutableTreeNode root, Tree tree) {
        LinkedList<DefaultMutableTreeNode> stack = new LinkedList<>();
        stack.push(root);
        List<DefaultMutableTreeNode> result = new ArrayList<>();

        while (!stack.isEmpty()) {
            DefaultMutableTreeNode node = stack.pop();
            if(opened && tree.isExpanded(new TreePath(node))){
                result.add(node);
            } else if(!opened && tree.isCollapsed(new TreePath(node))){
                result.add(node);
            }
            final Enumeration<TreeNode> children = node.children();
            while (children.hasMoreElements()){
                stack.push((DefaultMutableTreeNode) children.nextElement());
            }
        }

        return result;
    }
}
