package com.epam.esm.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagDto;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.CustomErrorCode;
import com.epam.esm.service.exception.GiftCertificateDtoValidationException;
import com.epam.esm.service.exception.NoSuchResourceException;
import com.epam.esm.service.mapper.CertificateDtoMapper;
import com.epam.esm.service.validation.PageInfoValidation;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceTest {

    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    @Mock
    private CertificateDtoMapper mapper;
    @Mock
    private PageInfoValidation pageValidation;

    @InjectMocks
    private GiftCertificateService giftCertificateService;

    public static final int OFFSET = 10;
    public static final int LIMIT = 10;
    public static final long NON_EXISTING_ID = 10000;
    private GiftCertificate certificate = new GiftCertificate(1L, "Spa", "Spa for company", new BigDecimal(150), 180,
            LocalDateTime.now(), LocalDateTime.now(), Arrays.asList(new Tag(1, "spa"), new Tag(2, "relax")));
    private GiftCertificateDto dto = new GiftCertificateDto(1L, "Spa", "Spa for company", new BigDecimal(150), 180,
            LocalDateTime.now(), LocalDateTime.now(), Arrays.asList(new TagDto(1, "spa"), new TagDto(2, "relax")));

    @Test
    void findAll() {
        GiftCertificate certificate = new GiftCertificate();
        List<GiftCertificate> certificates = Stream.of(certificate).collect(Collectors.toList());
        GiftCertificateDto certificateDTO = new GiftCertificateDto();
        List<GiftCertificateDto> expected = Stream.of(certificateDTO).collect(Collectors.toList());
        Mockito.when(giftCertificateRepository.findAll(OFFSET, LIMIT)).thenReturn(certificates);
        Mockito.when(mapper.changeCertificateToDto(certificate)).thenReturn(certificateDTO);
        assertEquals(expected, giftCertificateService.findAll(OFFSET, LIMIT));
    }

    @Test
    void findNumberOfEntities() {
        Mockito.when(giftCertificateRepository.findNumberOfEntities()).thenReturn(3L);
        assertEquals(giftCertificateService.findNumberOfEntities(), 3);
    }

    @Test
    void findByIdPositive() {
        Mockito.when(giftCertificateRepository.findById(certificate.getId())).thenReturn(certificate);
        Mockito.when(mapper.changeCertificateToDto(certificate)).thenReturn(dto);
        assertEquals(dto, giftCertificateService.findById(certificate.getId()));
    }

    @Test
    void findByIdNegative() {
        Mockito.when(giftCertificateRepository.findById(NON_EXISTING_ID)).thenReturn(null);
        assertThrows(NoSuchResourceException.class, () -> giftCertificateService.findById(NON_EXISTING_ID));
    }

    @Test
    void createPositive() {
        Mockito.when(giftCertificateRepository.create(certificate)).thenReturn(certificate);
        Mockito.when(mapper.changeDtoToCertificate(dto)).thenReturn(certificate);
        Mockito.when(mapper.changeCertificateToDto(certificate)).thenReturn(dto);
        assertEquals(dto, giftCertificateService.create(dto));
    }

    @Test
    void createNegative() {
        Mockito.when(pageValidation.checkPageInfo(OFFSET, LIMIT, CustomErrorCode.CERTIFICATE))
                .thenThrow(GiftCertificateDtoValidationException.class);
        Mockito.when(giftCertificateRepository.create(certificate)).thenReturn(certificate);
        Mockito.when(mapper.changeDtoToCertificate(dto)).thenReturn(certificate);
        Mockito.when(mapper.changeCertificateToDto(certificate)).thenReturn(dto);
        assertThrows(GiftCertificateDtoValidationException.class, () -> giftCertificateService.create(dto));
    }

    @Test
    void deletePositive() {
        Mockito.when(giftCertificateRepository.delete(certificate.getId())).thenReturn(certificate.getId());
        assertEquals(certificate.getId(), giftCertificateService.delete(certificate.getId()));
    }

    @Test
    void deleteNegative() {
        Mockito.when(giftCertificateRepository.delete(NON_EXISTING_ID)).thenThrow(RuntimeException.class);
        assertThrows(NoSuchResourceException.class, () -> giftCertificateService.delete(NON_EXISTING_ID));
    }

    @Test
    void updatePositive() {
        Mockito.when(giftCertificateRepository.update(certificate)).thenReturn(certificate);
        Mockito.when(mapper.changeCertificateToDto(certificate)).thenReturn(dto);
        Mockito.when(mapper.changeDtoToCertificate(dto)).thenReturn(certificate);
        Mockito.when(giftCertificateRepository.findById(certificate.getId())).thenReturn(certificate);
        assertEquals(dto, giftCertificateService.update(dto, certificate.getId()));
    }

    @Test
    void updateNegative() {
        Mockito.when(giftCertificateRepository.findById(NON_EXISTING_ID)).thenReturn(null);
        assertThrows(NoSuchResourceException.class, () -> giftCertificateService.update(dto, NON_EXISTING_ID));
    }
}