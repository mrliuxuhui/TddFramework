package com.flyingwillow.tdd.factory.factory;

import com.flyingwillow.tdd.factory.InterfaceDesigner;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class InterfaceWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        // show content
        InterfaceDesigner interfaceDesigner = new InterfaceDesigner(project, toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(interfaceDesigner.getMainPanel(), "", true);
        toolWindow.getContentManager().addContent(content);
        //


    }
}
