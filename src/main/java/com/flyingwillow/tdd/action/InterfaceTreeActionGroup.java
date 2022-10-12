package com.flyingwillow.tdd.action;

import com.flyingwillow.tdd.domain.InterfaceMetaInfo;
import com.flyingwillow.tdd.domain.InterfaceMetaType;
import com.flyingwillow.tdd.util.ToolkitUtil;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class InterfaceTreeActionGroup extends DefaultActionGroup {
    @Override
    public AnAction @NotNull [] getChildren(@Nullable AnActionEvent e) {
        final InterfaceMetaInfo metaInfo = ToolkitUtil.getMetaFromActionEvent(e);
        final ActionManager actionManager = ActionManager.getInstance();
        if (Objects.nonNull(metaInfo) && metaInfo.getType() == InterfaceMetaType.PACKAGE) {
            return new AnAction[]{actionManager.getAction("interface.popup.class.add"), actionManager.getAction("interface.popup.class.del")};
        } else if (Objects.nonNull(metaInfo) && metaInfo.getType() == InterfaceMetaType.CONTROLLER) {
            return new AnAction[]{actionManager.getAction("interface.popup.method.add"), actionManager.getAction("interface.popup.method.del")};
        } else if (Objects.nonNull(metaInfo) && metaInfo.getType() == InterfaceMetaType.INTERFACE) {
            return new AnAction[]{actionManager.getAction("interface.popup.method.edit")
                    , actionManager.getAction("interface.popup.method.edit.test")
                    , actionManager.getAction("interface.popup.method.del")};
        }
        return new AnAction[0];
    }
}
