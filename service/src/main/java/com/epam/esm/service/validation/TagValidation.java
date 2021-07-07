package com.epam.esm.service.validation;

import org.springframework.stereotype.Component;

import com.epam.esm.entity.TagDto;
import com.epam.esm.service.exception.TagValidationException;

@Component
public class TagValidation {

    public static final boolean DEFAULT_RESULT = true;

    public static final String INVALID_DTO_NAME = "tagdto_invalid_name";
    public static final int MIN_TAG_LENGTH = 3;
    public static final int MAX_TAG_LENGTH = 16;

    public boolean chechTagDtoFormat(TagDto dto) {
        boolean result = DEFAULT_RESULT;
        if (dto.getNameTag() == null || dto.getNameTag().isBlank() || dto.getNameTag().length() < MIN_TAG_LENGTH
                || dto.getNameTag().length() > MAX_TAG_LENGTH) {
            throw new TagValidationException(INVALID_DTO_NAME);
        }
        return result;
    }

}
