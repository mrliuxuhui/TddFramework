package com.flyingwillow.tdd.action;

import com.flyingwillow.tdd.domain.InterfaceMetaInfo;
import com.flyingwillow.tdd.domain.InterfaceMetaType;
import com.flyingwillow.tdd.editor.InterfaceVirtualFile;
import com.flyingwillow.tdd.util.ToolkitUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MethodEditAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final InterfaceMetaInfo metaInfo = ToolkitUtil.getMetaFromActionEvent(e);
        if (Objects.nonNull(metaInfo) && metaInfo.getType() == InterfaceMetaType.INTERFACE) {
            final InterfaceVirtualFile file = new InterfaceVirtualFile(metaInfo);
            FileEditorManager.getInstance(e.getProject()).openFile(file, true);
        }

    }
}
