package com.flyingwillow.tdd.factory;

import com.flyingwillow.tdd.domain.InterfaceMetaInfo;
import com.flyingwillow.tdd.domain.InterfaceMetaType;
import com.intellij.ide.dnd.TransferableList;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.tree.TreeUtil;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
public class InterfaceTreeTransferHandler extends TransferHandler {

    private Tree interfaceTree;

    public InterfaceTreeTransferHandler(Tree interfaceTree) {
        this.interfaceTree = interfaceTree;
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!support.isDrop()) {
            return false;
        }
        final TreePath[] selectionPaths = interfaceTree.getSelectionModel().getSelectionPaths();
        final Point dropPoint = support.getDropLocation().getDropPoint();
        final TreePath targetPath = TreeUtil.getPathForLocation(this.interfaceTree, dropPoint.x, dropPoint.y);
        if (Objects.isNull(targetPath) || null == selectionPaths || selectionPaths.length == 0) {
            return false;
        }
        final Object lastPathComponent = targetPath.getLastPathComponent();
        if (!(lastPathComponent instanceof DefaultMutableTreeNode)) {
            return false;
        }
        log.warn("target = " + targetPath);
        log.warn("selected = " + Arrays.toString(selectionPaths));
        final DefaultMutableTreeNode targetNode = (DefaultMutableTreeNode) lastPathComponent;
        final DefaultTreeModel model = (DefaultTreeModel) interfaceTree.getModel();
        Arrays.stream(selectionPaths).forEach(item -> {
            model.removeNodeFromParent(((DefaultMutableTreeNode) item.getLastPathComponent()));
            model.insertNodeInto((DefaultMutableTreeNode) item.getLastPathComponent(), targetNode, targetNode.getChildCount());
            interfaceTree.makeVisible(item);
        });
        return true;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        final Point dropPoint = support.getDropLocation().getDropPoint();
        final TreePath targetPath = TreeUtil.getPathForLocation(this.interfaceTree, dropPoint.x, dropPoint.y);
        final InterfaceMetaInfo target = (InterfaceMetaInfo) TreeUtil.getLastUserObject(targetPath);
        final TreePath[] selectionPaths = interfaceTree.getSelectionModel().getSelectionPaths();
        final List<InterfaceMetaInfo> sourceList = Arrays.stream(Optional.ofNullable(selectionPaths).orElse(new TreePath[0]))
                .map(e -> ((InterfaceMetaInfo) TreeUtil.getLastUserObject(e)))
                .collect(Collectors.toList());
        final List<InterfaceMetaType> types = sourceList.stream().map(InterfaceMetaInfo::getType).collect(Collectors.toList());
        final List<TreePath> parentPaths = Arrays.stream(Optional.ofNullable(selectionPaths).orElse(new TreePath[0]))
                .map(TreePath::getParentPath).collect(Collectors.toList());
        log.warn("----------------");
        log.warn("source types = " + types);
        log.warn("target type = " + (Objects.nonNull(target) ? target.getType() : ""));
        log.warn("source parent path = " + parentPaths);
        log.warn("target path = " + targetPath);
        log.warn("================");
        if (types.size() == 1 && Objects.nonNull(target)
                && InterfaceMetaType.match(types.get(0), target.getType())
                && parentPaths.stream().noneMatch(e -> e.equals(targetPath))) {
            log.info("can import = " + true);
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    protected Transferable createTransferable(JComponent c) {
        if (c instanceof JTree) {
            JTree tree = (JTree) c;
            TreePath[] selection = tree.getSelectionPaths();
            if (selection != null && selection.length >= 1) {
                return new TransferableList<TreePath>(selection) {
                    @Override
                    protected String toString(TreePath path) {
                        return String.valueOf(path.getLastPathComponent());
                    }
                };
            }
        }
        return null;
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        if (action == MOVE) {
            Arrays.stream(Optional.ofNullable(interfaceTree.getSelectionModel().getSelectionPaths()).orElse(new TreePath[0]))
                    .forEach(p -> {
                        ((DefaultMutableTreeNode) p.getParentPath().getLastPathComponent()).setAllowsChildren(true);
                        log.warn("to deleted = " + p);
                        interfaceTree.getSelectionModel().removeSelectionPath(p);
                    });
        }
    }

    @Override
    public int getSourceActions(JComponent c) {
        return MOVE;
    }
}
