package com.epam.esm.service.mapper;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateDto;

public interface CertificateDtoMapper {

    public GiftCertificate changeDtoToCertificate(GiftCertificateDto dto);

    public GiftCertificateDto changeCertificateToDto(GiftCertificate certificate);

}
