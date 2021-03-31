package com.epam.esm.service.exception;

public class CustomErrorExeption extends RuntimeException {

    private String code;

    public CustomErrorExeption(CustomErrorCode code) {
        this.code = code.getCode();
    }

    public CustomErrorExeption(String message, CustomErrorCode code) {
        super(message);
        this.code = code.getCode();
    }

    public String getCode() {
        return code;
    }
}
