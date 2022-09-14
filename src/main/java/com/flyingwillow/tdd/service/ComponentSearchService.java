package com.flyingwillow.tdd.service;

import com.flyingwillow.tdd.factory.InterfaceDesignerContent;
import com.intellij.openapi.components.Service;

@Service
public final class ComponentSearchService {

    private InterfaceDesignerContent content;

    public void setContent(InterfaceDesignerContent content) {
        this.content = content;
    }

    public InterfaceDesignerContent getContent() {
        return content;
    }
}
