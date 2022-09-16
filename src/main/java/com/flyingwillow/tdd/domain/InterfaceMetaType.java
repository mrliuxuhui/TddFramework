package com.flyingwillow.tdd.domain;

public enum InterfaceMetaType {
    ROOT(0, "root"),
    PACKAGE(1, "package"),
    CONTROLLER(2, "controller"),
    INTERFACE(3, "interface");


    private int value;
    private String desc;

    InterfaceMetaType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
