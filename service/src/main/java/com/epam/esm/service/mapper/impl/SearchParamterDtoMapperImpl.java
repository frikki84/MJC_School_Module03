package com.epam.esm.service.mapper.impl;

import org.springframework.stereotype.Component;

import com.epam.esm.entity.SearchGiftCertificateParameter;
import com.epam.esm.entity.SearchGiftCertificateParameterDto;
import com.epam.esm.service.mapper.SearchParamterDtoMapper;

@Component
public class SearchParamterDtoMapperImpl implements SearchParamterDtoMapper {

    @Override
    public SearchGiftCertificateParameter changeDtoToSearchGiftSertificateParametr(
            SearchGiftCertificateParameterDto dto) {
        SearchGiftCertificateParameter parametr = new SearchGiftCertificateParameter();
        parametr.setName(dto.getName());
        parametr.setDescription(dto.getDescription());
        parametr.setTags(dto.getTags());
        parametr.setOrder(dto.getOrder());
        parametr.setSortBy(dto.getSortBy());
        return parametr;
    }
}
