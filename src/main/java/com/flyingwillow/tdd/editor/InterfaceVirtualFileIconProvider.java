package com.flyingwillow.tdd.editor;

import com.intellij.ide.FileIconProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import icons.JavaUltimateIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class InterfaceVirtualFileIconProvider implements FileIconProvider {
    @Override
    public @Nullable Icon getIcon(@NotNull VirtualFile file, int flags, @Nullable Project project) {
        if(file instanceof InterfaceVirtualFile){
            return JavaUltimateIcons.Web.RequestMapping;
        }
        return null;
    }
}
