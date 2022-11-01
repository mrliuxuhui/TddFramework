package com.flyingwillow.tdd.provider;

import com.flyingwillow.tdd.domain.FieldDataType;
import com.flyingwillow.tdd.domain.InterfaceFormData;
import com.flyingwillow.tdd.domain.InterfaceMetaInfo;
import com.flyingwillow.tdd.domain.ParameterItem;
import com.flyingwillow.tdd.domain.ParameterType;
import com.flyingwillow.tdd.util.HttpMethodEnum;
import com.flyingwillow.tdd.util.ToolkitUtil;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiTypeElement;
import com.intellij.util.ui.ColumnInfo;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultInterfaceDataProvider implements InterfaceDataProvider {

    private InterfaceMetaInfo metaInfo;

    public DefaultInterfaceDataProvider(InterfaceMetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    @Override
    public InterfaceFormData getFormData() {
        return new InterfaceFormData(metaInfo);
    }

    @Override
    public ComboBoxModel getHttpMethodModel() {
        return new DefaultComboBoxModel<>(HttpMethodEnum.names().toArray(new String[]{}));
    }

    @Override
    public String getInputParamTitle() {
        return "入参配置";
    }

    @Override
    public String getOutputParamTitle() {
        return "出参配置";
    }

    @Override
    public DefaultMutableTreeNode getParameterRootNode(ParameterType type) {
        if (type == ParameterType.OUTPUT) {
            return getOutputParameter();
        } else {
            return getInputParameter();
        }
    }

    private DefaultMutableTreeNode getInputParameter() {
        if (!(metaInfo.getTarget() instanceof PsiMethod)) {
            return null;
        }
        PsiMethod method = (PsiMethod) metaInfo.getTarget();
        final List<ParameterItem> parameterItemList = Arrays.stream(method.getParameterList().getParameters())
                .map(p -> new ParameterItem(p, metaInfo.getModule(), ParameterReaderFactory.INSTANCE.getParameterReader(p)))
                .collect(Collectors.toList());
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        root.setAllowsChildren(true);
        if (parameterItemList.size() != 1 || parameterItemList.get(0).getType() != FieldDataType.OBJECT) {
            root.setUserObject(ParameterItem.createRoot());
            parameterItemList.forEach(e -> root.add(new DefaultMutableTreeNode(e, e.isHasChildren())));
        } else {
            root.setUserObject(parameterItemList.get(0));
        }
        return root;
    }

    @Override
    public ColumnInfo[] getColumns() {
        return TableColumnDefinition.DEFAULT_COLUMNS;
    }

    private DefaultMutableTreeNode getOutputParameter() {
        if (!(metaInfo.getTarget() instanceof PsiMethod)) {
            return new DefaultMutableTreeNode();
        }
        @Nullable PsiTypeElement returnType = ((PsiMethod) metaInfo.getTarget()).getReturnTypeElement();
        boolean simpleType = ToolkitUtil.isSimpleType(returnType.getType());
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        root.setAllowsChildren(!simpleType);
        root.setUserObject(new ParameterItem(returnType, metaInfo.getModule(), ParameterReaderFactory.INSTANCE.getParameterReader(returnType.getType())));
        return root;
    }
}
