package com.flyingwillow.tdd.provider;

import com.flyingwillow.tdd.domain.FieldDataType;
import com.flyingwillow.tdd.domain.ParameterItem;
import com.intellij.ide.util.scopeChooser.PackageSetChooserCombo;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.psi.search.scope.packageSet.CustomScopesProviderEx;
import com.intellij.psi.search.scope.packageSet.NamedScope;
import com.intellij.ui.BooleanTableCellEditor;
import com.intellij.ui.components.editors.JBComboBoxTableCellEditorComponent;
import com.intellij.util.ui.AbstractTableCellEditor;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.table.ComboBoxTableCellEditor;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class TableColumnDefinition<T> extends ColumnInfo<DefaultMutableTreeNode, T> {

    private ControlType controlType;
    private Function<ParameterItem, T> reader;

    private BiConsumer<ParameterItem, T> writer;

    private List<String> options;

    private static final TableColumnDefinition<String> COLUMN_NAME = new TableColumnDefinition<>("字段名"
            , ControlType.INPUT
            , ParameterItem::getName
            , ParameterItem::setName);

    private static final TableColumnDefinition<FieldDataType> COLUMN_DATA_TYPE = new TableColumnDefinition<>("字段类型"
            , ControlType.SELECT
            , ParameterItem::getType
            , ParameterItem::setType
            , Arrays.stream(FieldDataType.values()).map(FieldDataType::name).collect(Collectors.toList()));
    private static final TableColumnDefinition<String> COLUMN_DEFAULT_VALUE = new TableColumnDefinition<>("字段默认值"
            , ControlType.INPUT
            , ParameterItem::getDefaultValue
            , ParameterItem::setDefaultValue);
    private static final TableColumnDefinition<String> COLUMN_COMMENT = new TableColumnDefinition<>("字段描述"
            , ControlType.INPUT
            , ParameterItem::getComment
            , ParameterItem::setComment);
    private static final TableColumnDefinition<Boolean> COLUMN_REQUIRED = new TableColumnDefinition<>("是否必须"
            , ControlType.RADIO
            , ParameterItem::isRequired
            , ParameterItem::setRequired
            , Arrays.asList("true", "false"));
    private static final TableColumnDefinition<String> COLUMN_CLASS = new TableColumnDefinition<>("类名"
            , ControlType.PACKAGE_SELECT
            , ParameterItem::getQualifiedClassName
            , ParameterItem::setQualifiedClassName);

    public static final TableColumnDefinition[] DEFAULT_COLUMNS = new TableColumnDefinition[]{
            COLUMN_NAME, COLUMN_DATA_TYPE, COLUMN_DEFAULT_VALUE, COLUMN_COMMENT, COLUMN_REQUIRED, COLUMN_CLASS
    };

    public TableColumnDefinition(@NlsContexts.ColumnName String name, ControlType type
            , Function<ParameterItem, T> reader
            , BiConsumer<ParameterItem, T> writer) {
        super(name);
        this.controlType = type;
        this.reader = reader;
        this.writer = writer;
    }

    public TableColumnDefinition(@NlsContexts.ColumnName String name, ControlType controlType
            , Function<ParameterItem, T> reader
            , BiConsumer<ParameterItem, T> writer
            , List<String> options) {
        super(name);
        this.controlType = controlType;
        this.reader = reader;
        this.writer = writer;
        this.options = options;
    }

    @Override
    public @Nullable T valueOf(DefaultMutableTreeNode defaultMutableTreeNode) {
        final ParameterItem parameterItem = getParameterItem(defaultMutableTreeNode);
        if (Objects.nonNull(parameterItem)) {
            return reader.apply(parameterItem);
        } else {
            return null;
        }

    }

    @Override
    public TableCellEditor getEditor(DefaultMutableTreeNode o) {
        if (controlType == ControlType.INPUT) {
            return new DefaultCellEditor(new JTextField());
        } else if (controlType == ControlType.SELECT) {
            return new ComboBoxTableCellEditor();
        } else if (controlType == ControlType.RADIO) {
            return new BooleanTableCellEditor();
        } else if (controlType == ControlType.PACKAGE_SELECT) {
            return new PackageChooserComboBox((ParameterItem) o.getUserObject());
        }
        return new DefaultCellEditor(new JTextField());
    }

    @Override
    public void setValue(DefaultMutableTreeNode o, T value) {
        final ParameterItem userObject = getParameterItem(o);
        if (Objects.nonNull(userObject)) {
            this.writer.accept(userObject, value);
        }
    }

    @Override
    public boolean isCellEditable(DefaultMutableTreeNode o) {
        ParameterItem parameterItem = getParameterItem(o);
        if (o.isRoot()) {
            return false;
        } else if ((controlType == ControlType.INPUT
                || controlType == ControlType.SELECT
                || controlType == ControlType.RADIO)
                && Objects.nonNull(valueOf(o))) {
            return true;
        } else if (controlType == ControlType.PACKAGE_SELECT
                && Objects.nonNull(parameterItem)
                && parameterItem.getType() == FieldDataType.OBJECT) {
            return true;
        } else {
            return false;
        }
    }

    private ParameterItem getParameterItem(DefaultMutableTreeNode o) {
        if (Objects.nonNull(o) && Objects.nonNull(o.getUserObject())
                && (o.getUserObject() instanceof ParameterItem)) {
            return (ParameterItem) o.getUserObject();
        } else {
            return null;
        }
    }

    public enum ControlType {
        INPUT,
        SELECT,
        RADIO,
        PACKAGE_SELECT,
        READONLY_INPUT;
    }

    public class PackageChooserComboBox extends AbstractTableCellEditor {

        private PackageSetChooserCombo myScopeChooser;
        private ParameterItem parameterItem;

        public PackageChooserComboBox(ParameterItem parameterItem) {
            this.parameterItem = parameterItem;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            myScopeChooser = new PackageSetChooserCombo(parameterItem.getProject(), value == null ? null : ((NamedScope) value).getScopeId(), false, false) {
                @Override
                protected NamedScope[] createModel() {
                    final NamedScope[] model = super.createModel();
                    final ArrayList<NamedScope> filteredScopes = new ArrayList<>(Arrays.asList(model));
                    CustomScopesProviderEx.filterNoSettingsScopes(parameterItem.getProject(), filteredScopes);
                    return filteredScopes.toArray(NamedScope.EMPTY_ARRAY);
                }
            };

            ((JBComboBoxTableCellEditorComponent) myScopeChooser.getChildComponent()).setCell(table, row, column);
            return myScopeChooser;
        }

        @Override
        public Object getCellEditorValue() {
            return myScopeChooser.getSelectedScope();
        }
    }
}
