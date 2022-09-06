package com.flyingwillow.tdd.domain;

import com.intellij.openapi.project.Project;
import com.intellij.packageDependencies.ui.TreeModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class InterfaceMetaInfo {

    private Integer id;
    private String name;
    private Integer parentId;
    private Integer level;
    private Boolean leaf = true;

    public InterfaceMetaInfo() {
    }

    public InterfaceMetaInfo(Integer id, String name, Integer parentId, Integer level, Boolean leaf) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.level = level;
        this.leaf = leaf;
    }

    public static TreeModel buildTree(List<InterfaceMetaInfo> list, Project project) {
        if (CollectionUtils.isEmpty(list)) {
            return new TreeModel(null);
        }
        InterfaceMetaInfo rootInfo = new InterfaceMetaInfo();
        rootInfo.setName(project.getName());
        rootInfo.setParentId(0);
        rootInfo.setLevel(0);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootInfo);
        final List<DefaultMutableTreeNode> nodeList = list.stream().map(e -> new DefaultMutableTreeNode(e, !e.leaf)).collect(Collectors.toList());
        final Map<String, DefaultMutableTreeNode> infoMap = nodeList.stream()
                .collect(Collectors.toMap(e -> String.valueOf(((InterfaceMetaInfo) e.getUserObject()).getId()), Function.identity()));

        nodeList.forEach(node -> {
            final InterfaceMetaInfo meta = (InterfaceMetaInfo) node.getUserObject();
            final Integer level = meta.getLevel();
            if (1 == level) {
                root.add(node);
                node.setParent(root);
            } else {
                final DefaultMutableTreeNode parent = infoMap.get(String.valueOf(meta.getId()));
                parent.add(node);
                node.setParent(parent);
            }
        });

        return new TreeModel(root);
    }
}
