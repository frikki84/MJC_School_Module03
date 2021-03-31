package com.epam.esm.service.validation;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.service.exception.GiftCertificateDtoValidationException;

@Component
public class GiftCertificateDtoValidation {

    private static TagValidation tagValidation;

    public static final boolean DEFAULT_RESULT = true;

    public static final String INVALID_DTO_NAME = "dto_invalid_name";
    public static final String INVALID_DTO_DECRIPTION = "dto_invalid_description";
    public static final String INVALID_DTO_PRICE = "dto_invalid_price";
    public static final String INVALID_DTO_DURATION = "dto_invalid_duration";
    public static final String INVALID_DTO_TAG_LIST = "dto_invalid_taglist";

    public static final int MIN_NAME_LENGTH = 3;
    public static final int MAX_NAME_LENGTH = 32;
    public static final int MIN_DESCRIPTION_LENGTH = 5;
    public static final int MAX_DESCRIPTION_LENGTH = 100;
    public static final String MIN_PRICE = "0";
    public static final String MIN_DURATION = "1";

    public GiftCertificateDtoValidation(TagValidation tagValidation) {
        this.tagValidation = tagValidation;
    }

    public static boolean chechCertificateDtoFormat(GiftCertificateDto dto) {
        boolean result = DEFAULT_RESULT;
        if (Objects.isNull(dto.getName()) || dto.getName().isBlank() || dto.getName().length() < MIN_NAME_LENGTH
                || dto.getName().length() > MAX_NAME_LENGTH) {
            throw new GiftCertificateDtoValidationException(INVALID_DTO_NAME);
        } else if (Objects.isNull(dto.getDescription()) || dto.getDescription().isBlank()
                || dto.getDescription().length() < MIN_DESCRIPTION_LENGTH
                || dto.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new GiftCertificateDtoValidationException(INVALID_DTO_DECRIPTION);
        } else if (Objects.isNull(dto.getPrice()) || dto.getPrice().compareTo(new BigDecimal(MIN_PRICE)) < 0) {
            throw new GiftCertificateDtoValidationException(INVALID_DTO_PRICE);
        } else if (Objects.isNull(dto.getDuration()) || dto.getDuration().compareTo(Integer.parseInt(MIN_DURATION)) < 0) {
            throw new GiftCertificateDtoValidationException(INVALID_DTO_DURATION);
        } else if (Objects.isNull(dto.getTagList()) || dto.getTagList().isEmpty()) {
            throw new GiftCertificateDtoValidationException(INVALID_DTO_TAG_LIST);
        }

        return result;
    }

    public  boolean chechCertificateDtoFormatForUpdate(GiftCertificateDto dto) {
        boolean result = DEFAULT_RESULT;
        if (Objects.nonNull(dto.getName()) && (dto.getName().length() < MIN_NAME_LENGTH
                || dto.getName().length() > MAX_NAME_LENGTH)) {
            throw new GiftCertificateDtoValidationException(INVALID_DTO_NAME);
        }
        if (Objects.nonNull(dto.getDescription()) && (dto.getDescription().length() < MIN_DESCRIPTION_LENGTH
                || dto.getDescription().length() > MAX_DESCRIPTION_LENGTH)) {
            throw new GiftCertificateDtoValidationException(INVALID_DTO_DECRIPTION);
        }
        if (Objects.nonNull(dto.getPrice()) && dto.getPrice().compareTo(new BigDecimal(MIN_PRICE)) < 0) {
            throw new GiftCertificateDtoValidationException(INVALID_DTO_PRICE);
        }
        if (Objects.nonNull(dto.getDuration()) && dto.getDuration().compareTo(Integer.parseInt(MIN_DURATION)) < 0) {
            throw new GiftCertificateDtoValidationException(INVALID_DTO_DURATION);
        }
        if (Objects.nonNull(dto.getTagList()) && !dto.getTagList().isEmpty()) {
            dto.getTagList().forEach(tagDto -> tagValidation.chechTagDtoFormat(tagDto));
        }
        return result;
    }

}
