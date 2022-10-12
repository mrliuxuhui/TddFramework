package com.flyingwillow.tdd.provider;

import com.flyingwillow.tdd.domain.InterfaceFormData;
import com.flyingwillow.tdd.domain.InterfaceMetaInfo;
import com.flyingwillow.tdd.util.HttpMethodEnum;

import javax.swing.*;

public class DefaultInterfaceDataProvider implements InterfaceDataProvider{

    private InterfaceMetaInfo metaInfo;

    public DefaultInterfaceDataProvider(InterfaceMetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    @Override
    public InterfaceFormData getFormData() {
        return new InterfaceFormData(metaInfo);
    }

    @Override
    public ComboBoxModel getHttpMethodModel() {
        return new DefaultComboBoxModel<>(HttpMethodEnum.names().toArray(new String[]{}));
    }

    @Override
    public String getInputParamTitle() {
        return "入参配置";
    }

    @Override
    public String getOutputParamTitle() {
        return "出参配置";
    }
}
