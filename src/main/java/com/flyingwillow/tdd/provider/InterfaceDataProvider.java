package com.flyingwillow.tdd.provider;

import com.flyingwillow.tdd.domain.InterfaceFormData;

import javax.swing.*;

public interface InterfaceDataProvider {
    InterfaceFormData getFormData();

    ComboBoxModel<String> getHttpMethodModel();

    String getInputParamTitle();

    String getOutputParamTitle();
}
