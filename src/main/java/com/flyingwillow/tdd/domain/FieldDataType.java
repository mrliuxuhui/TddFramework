package com.flyingwillow.tdd.domain;

import java.util.List;
import java.util.Map;

public enum FieldDataType {
    OBJECT("Object", new Class[]{}),
    STRING("String", new Class[]{String.class}),
    INTEGER("Integer", new Class[]{Integer.class, int.class}),
    SHORT("Short", new Class[]{Short.class, short.class}),
    LONG("Long", new Class[]{Long.class, long.class}),
    DOUBLE("Double", new Class[]{Double.class, double.class}),
    FLOAT("Float", new Class[]{Float.class, float.class}),
    BOOLEAN("Boolean", new Class[]{Boolean.class, boolean.class}),
    LIST("List", new Class[]{List.class}),
    MAP("Map", new Class[]{Map.class});

    private String name;
    private Class[] classes;

    FieldDataType(String name, Class[] classes) {
        this.name = name;
        this.classes = classes;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
