package com.epam.esm.service.exception;

public enum CustomErrorCode {

    CERTIFICATE("01", "Certificate"), TAG("02", "Tag"), USER("03", "User"), ORDER("04", "Order"), GENERAL("05", "General");

    private String code;
    public String name;

    private CustomErrorCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
