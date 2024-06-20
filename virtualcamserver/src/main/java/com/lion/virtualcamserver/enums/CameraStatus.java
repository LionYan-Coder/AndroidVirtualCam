package com.lion.virtualcamserver.enums;

public enum CameraStatus {
    ENABLED("enabled"),DISABLE("disable");

    private String value;

    private CameraStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
