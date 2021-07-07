package com.epam.esm.service.validation;

import org.springframework.stereotype.Component;

import com.epam.esm.repository.CrdOperations;
import com.epam.esm.service.exception.CustomErrorCode;
import com.epam.esm.service.exception.PageException;

@Component
public class PageInfoValidation {

    public static final int MIN_PAGE_VALUE = 1;
    public static final int MIN_PAGE_SIZE = 1;
    public static final int VALUE_TO_CHANGE_FIRST_INDEX_FROM_ONE_TO_ZERO = 1;

    public static final String INVALID_PAGE_NUMBER = "page_invalid_number";
    public static final String NONEXISTENT_PAGE_SIZE = "page_wrong_size";
    public static final String NONEXISTENT_PAGE_NUMBER = "page_wrong_page_number";

    private CrdOperations crdOperations;

    public void setCrdOperations(CrdOperations crdOperations) {
        this.crdOperations = crdOperations;
    }

    public boolean checkPageInfo(int offset, int limit, CustomErrorCode errorCode) {
        boolean result = true;

        if (offset < MIN_PAGE_VALUE) {
            throw new PageException(INVALID_PAGE_NUMBER, errorCode);
        }
        if (limit < MIN_PAGE_SIZE) {
            throw new PageException(NONEXISTENT_PAGE_SIZE, errorCode);
        }
        if ((offset - VALUE_TO_CHANGE_FIRST_INDEX_FROM_ONE_TO_ZERO) * limit > crdOperations.findNumberOfEntities()) {
            throw new PageException(NONEXISTENT_PAGE_NUMBER, errorCode);
        }

        return result;
    }
}
