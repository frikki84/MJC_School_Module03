package com.epam.esm.service.mapper;

import com.epam.esm.entity.SearchGiftCertificateParameter;
import com.epam.esm.entity.SearchGiftCertificateParameterDto;

public interface SearchParamterDtoMapper {

    public SearchGiftCertificateParameter changeDtoToSearchGiftSertificateParametr(
            SearchGiftCertificateParameterDto dto);

}
