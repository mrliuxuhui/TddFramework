package com.flyingwillow.tdd.provider;

import com.flyingwillow.tdd.domain.FieldDataType;
import com.flyingwillow.tdd.domain.ParameterItem;
import com.flyingwillow.tdd.resource.InterfaceBundle;
import com.intellij.ide.util.PackageUtil;
import com.intellij.java.JavaBundle;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.refactoring.move.moveClassesOrPackages.DestinationFolderComboBox;
import com.intellij.refactoring.ui.PackageNameReferenceEditorCombo;
import com.intellij.ui.BooleanTableCellEditor;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import com.intellij.util.ui.AbstractTableCellEditor;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.table.ComboBoxTableCellEditor;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
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

    private static final TableColumnDefinition<String> COLUMN_NAME = new TableColumnDefinition<>(
            InterfaceBundle.message("interface.edit.panel.params.table.field.name")
            , ControlType.INPUT
            , ParameterItem::getName
            , ParameterItem::setName);

    private static final TableColumnDefinition<FieldDataType> COLUMN_DATA_TYPE = new TableColumnDefinition<>(
            InterfaceBundle.message("interface.edit.panel.params.table.field.type")
            , ControlType.SELECT
            , ParameterItem::getType
            , ParameterItem::setType
            , Arrays.stream(FieldDataType.values()).map(FieldDataType::name).collect(Collectors.toList()));
    private static final TableColumnDefinition<String> COLUMN_DEFAULT_VALUE = new TableColumnDefinition<>(
            InterfaceBundle.message("interface.edit.panel.params.table.field.default")
            , ControlType.INPUT
            , ParameterItem::getDefaultValue
            , ParameterItem::setDefaultValue);
    private static final TableColumnDefinition<String> COLUMN_COMMENT = new TableColumnDefinition<>(
            InterfaceBundle.message("interface.edit.panel.params.table.field.desc")
            , ControlType.INPUT
            , ParameterItem::getComment
            , ParameterItem::setComment);
    private static final TableColumnDefinition<Boolean> COLUMN_REQUIRED = new TableColumnDefinition<>(
            InterfaceBundle.message("interface.edit.panel.params.table.field.required")
            , ControlType.RADIO
            , ParameterItem::isRequired
            , ParameterItem::setRequired
            , Arrays.asList("true", "false"));
    private static final TableColumnDefinition<String> COLUMN_PACKAGE = new TableColumnDefinition<>(
            InterfaceBundle.message("interface.edit.panel.params.table.field.package")
            , ControlType.PACKAGE_SELECT
            , ParameterItem::getPackageName
            , ParameterItem::setPackageName);
    private static final TableColumnDefinition<String> COLUMN_CLASS = new TableColumnDefinition<>(
            InterfaceBundle.message("interface.edit.panel.params.table.field.class")
            , ControlType.INPUT
            , ParameterItem::getClassName
            , ParameterItem::setClassName);

    public static final TableColumnDefinition[] DEFAULT_COLUMNS = new TableColumnDefinition[]{
            COLUMN_NAME, COLUMN_DATA_TYPE, COLUMN_DEFAULT_VALUE, COLUMN_COMMENT, COLUMN_REQUIRED, COLUMN_PACKAGE, COLUMN_CLASS
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
        if (o.isRoot() && Objects.nonNull(parameterItem) && parameterItem.isRoot()) {
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

        private ParameterItem parameterItem;

        private DestinationFolderComboBox myPackageChooser;

        public PackageChooserComboBox(ParameterItem parameterItem) {
            this.parameterItem = parameterItem;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            ReferenceEditorComboWithBrowseButton myPackageComponent = new PackageNameReferenceEditorCombo(parameterItem.getPackageName()
                    , parameterItem.getProject()
                    , "CreateClassDialog.RecentsKey"
                    , JavaBundle.message("dialog.create.class.package.chooser.title"));
            myPackageComponent.setTextFieldPreferredWidth(40);
            myPackageChooser = new DestinationFolderComboBox() {
                @Override
                public String getTargetPackage() {
                    return parameterItem.getPackageName();
                }
            };
            myPackageChooser.setData(parameterItem.getProject()
                    , PackageUtil.findPossiblePackageDirectoryInModule(parameterItem.getModule(), parameterItem.getPackageName())
                    , myPackageComponent.getChildComponent());
            return myPackageChooser;
        }

        @Override
        public Object getCellEditorValue() {
            return myPackageChooser.getTargetPackage();
        }
    }
}
