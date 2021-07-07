package com.epam.esm.service.exception;

public class PageException extends CustomErrorExeption {

    public PageException(CustomErrorCode code) {
        super(code);
    }

    public PageException(String message, CustomErrorCode code) {
        super(message, code);
    }
}
