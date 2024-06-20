package com.lion.virtualcamserver.enums;

public enum ClientType {
    CONTROLLER("controller"),RECEIVER("receiver");

    private String value;

    private ClientType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
