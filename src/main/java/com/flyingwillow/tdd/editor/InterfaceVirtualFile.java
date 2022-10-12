package com.flyingwillow.tdd.editor;

import com.flyingwillow.tdd.domain.InterfaceMetaInfo;
import com.flyingwillow.tdd.provider.DefaultInterfaceDataProvider;
import com.flyingwillow.tdd.provider.InterfaceDataProvider;
import com.intellij.testFramework.LightVirtualFile;

public class InterfaceVirtualFile extends LightVirtualFile {

    private InterfaceMetaInfo metaInfo;

    private InterfaceDataProvider dataProvider;

    public InterfaceVirtualFile(InterfaceMetaInfo metaInfo) {
        super();
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
