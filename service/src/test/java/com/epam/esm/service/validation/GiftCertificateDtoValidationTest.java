package com.epam.esm.service.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.entity.TagDto;
import com.epam.esm.service.exception.GiftCertificateDtoValidationException;

@ExtendWith(MockitoExtension.class)
class GiftCertificateDtoValidationTest {

    private static final Long ID = 1l;
    private static final String VALID_NAME = "GiftCertificate valid name";
    private static final String VALID_DESCRIPTION = "GiftCertificate valid description";
    private static final BigDecimal VALID_PRICE = new BigDecimal(100);
    private static final Integer VALID_DURATION = 100;
    private static List<TagDto> tagList = Arrays.asList(new TagDto(1, "Tag"));

    private GiftCertificateDto validDto = new GiftCertificateDto(ID, VALID_NAME, VALID_DESCRIPTION, VALID_PRICE,
            VALID_DURATION, LocalDateTime.now(), LocalDateTime.now(), tagList);
    private GiftCertificateDto invalidDto = new GiftCertificateDto();

    @Test
    void chechCertificateDtoFormat() {
        assertEquals(true, GiftCertificateDtoValidation.chechCertificateDtoFormat(validDto));
    }

    @Test
    void chechCertificateDtoFormatNegative() {
        assertThrows(GiftCertificateDtoValidationException.class,
                () -> GiftCertificateDtoValidation.chechCertificateDtoFormat(invalidDto));
    }
}