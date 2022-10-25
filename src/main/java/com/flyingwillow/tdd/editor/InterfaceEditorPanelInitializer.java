package com.flyingwillow.tdd.editor;

import com.flyingwillow.tdd.provider.InterfaceDataProvider;

public interface InterfaceEditorPanelInitializer {

    void initPanel(InterfaceDataProvider dataProvider);

    void updateData(InterfaceDataProvider dataProvider);

    void setDataProvider(InterfaceDataProvider dataProvider);
}
