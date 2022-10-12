package com.flyingwillow.tdd.editor;

import com.flyingwillow.tdd.service.InterfaceEditorService;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class InterfaceEditorProvider implements FileEditorProvider, DumbAware {

    public static final String INTERFACE_EDITOR = "com.flyingwillow.interface.editor";

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return file instanceof InterfaceVirtualFile;
    }

    @Override
    public @NotNull FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        final InterfaceEditorService service = project.getService(InterfaceEditorService.class);
        final InterfaceEditor editor = service.getInstance();
        editor.setFile((InterfaceVirtualFile) file);
        editor.updateData(((InterfaceVirtualFile) file).getDataProvider());
        return editor;
    }

    @Override
    public @NotNull
    @NonNls String getEditorTypeId() {
        return INTERFACE_EDITOR;
    }

    @Override
    public @NotNull FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }
}
