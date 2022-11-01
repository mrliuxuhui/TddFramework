package com.flyingwillow.tdd.editor;

import com.flyingwillow.tdd.domain.InterfaceMetaInfo;
import com.flyingwillow.tdd.provider.DefaultInterfaceDataProvider;
import com.flyingwillow.tdd.provider.InterfaceDataProvider;
import com.intellij.openapi.vfs.VirtualFileWithoutContent;
import com.intellij.testFramework.LightVirtualFile;

public class InterfaceVirtualFile extends LightVirtualFile implements VirtualFileWithoutContent {

    private InterfaceMetaInfo metaInfo;

    private InterfaceDataProvider dataProvider;

    public InterfaceVirtualFile(InterfaceMetaInfo metaInfo) {
        super(metaInfo.getName());
        this.metaInfo = metaInfo;
        this.dataProvider = new DefaultInterfaceDataProvider(this.metaInfo);
    }

    public InterfaceMetaInfo getMetaInfo() {
        return metaInfo;
    }

    public InterfaceDataProvider getDataProvider() {
        return dataProvider;
    }
}
