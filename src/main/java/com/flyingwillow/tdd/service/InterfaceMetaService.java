package com.flyingwillow.tdd.service;

import com.flyingwillow.tdd.domain.InterfaceMetaInfo;
import com.flyingwillow.tdd.domain.InterfaceMetaType;
import com.google.common.collect.ImmutableList;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;

import java.util.List;

@Service
public final class InterfaceMetaService {

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
}
