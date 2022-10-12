package com.flyingwillow.tdd.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum HttpMethodEnum {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");


    private String name;

    HttpMethodEnum(String name) {
        this.name = name;
    }

    public static List<String> names() {
        return Arrays.stream(values()).map(HttpMethodEnum::name).collect(Collectors.toList());
    }
}
