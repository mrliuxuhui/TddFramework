package com.flyingwillow.tdd.service;

import com.flyingwillow.tdd.editor.InterfaceEditor;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Service
@Slf4j
public final class InterfaceEditorService {

    private InterfaceEditor instance;
    private Project project;

    public InterfaceEditorService(Project project){
        this.project = project;
    }
}
