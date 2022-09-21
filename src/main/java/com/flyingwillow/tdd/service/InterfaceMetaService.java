package com.flyingwillow.tdd.service;

import com.flyingwillow.tdd.domain.InterfaceMetaInfo;
import com.flyingwillow.tdd.domain.InterfaceMetaType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public final class InterfaceMetaService {

    private static final Map<String, String> ERROR_MSG = ImmutableMap.<String, String>builder()
            .put("-1", "本工程不包含spring web模块,请确认所有controller是否放置在xxx.controller包下")
            .put("2", "")
            .build();

    public List<InterfaceMetaInfo> selectAll(Project project) {
        return ImmutableList.<InterfaceMetaInfo>builder()
                .add(new InterfaceMetaInfo(1, "第一级", 0, 1, false, InterfaceMetaType.PACKAGE))
                .add(new InterfaceMetaInfo(2, "第二级1", 1, 2, false, InterfaceMetaType.CONTROLLER))
                .add(new InterfaceMetaInfo(3, "第二级2", 1, 2, true, InterfaceMetaType.INTERFACE))
                .add(new InterfaceMetaInfo(4, "第二级3", 1, 2, false, InterfaceMetaType.CONTROLLER))
                .add(new InterfaceMetaInfo(5, "第三级1", 2, 3, true, InterfaceMetaType.INTERFACE))
                .add(new InterfaceMetaInfo(6, "第三级2", 4, 4, true, InterfaceMetaType.INTERFACE))
                .build();

//        return Collections.emptyList();
    }

    private boolean isValidModule(Module module) {
        return false;
    }

    public String getErrorMsg(int status) {
        return ERROR_MSG.get(String.valueOf(status));
    }

    public List<Module> getValidModules(Project project) {
        return Arrays.stream(ModuleManager.getInstance(project).getModules())
                .filter(this::isValidModule)
                .collect(Collectors.toList());
    }
}
