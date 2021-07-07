package com.epam.esm.service.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagDto;
import com.epam.esm.service.mapper.CertificateDtoMapper;
import com.epam.esm.service.mapper.TagDtoMapper;

@Component
public class CertificateDtoMapperImpl implements CertificateDtoMapper {

    @Autowired
    private final TagDtoMapper tagDtoMapper;

    public CertificateDtoMapperImpl(TagDtoMapper tagDtoMapper) {
        this.tagDtoMapper = tagDtoMapper;
    }

    @Override
    public GiftCertificate changeDtoToCertificate(GiftCertificateDto dto) {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(dto.getId());
        certificate.setName(dto.getName());
        certificate.setDescription(dto.getDescription());
        certificate.setPrice(dto.getPrice());
        certificate.setDuration(dto.getDuration());
        certificate.setCreateDate(dto.getCreateDate());
        certificate.setLastUpdateDate(dto.getLastUpdateDate());
        List<Tag> tagList = new ArrayList<>();
        if (Objects.nonNull(dto.getTagList())) {
            dto.getTagList().forEach(tagDto -> tagList.add(tagDtoMapper.changeTagDtoToTag(tagDto)));
        }
        certificate.setTags(tagList);
        return certificate;
    }

    @Override
    public GiftCertificateDto changeCertificateToDto(GiftCertificate certificate) {
        GiftCertificateDto dto = new GiftCertificateDto();
        dto.setId(certificate.getId());
        dto.setName(certificate.getName());
        dto.setDescription(certificate.getDescription());
        dto.setPrice(certificate.getPrice());
        dto.setDuration(certificate.getDuration());
        dto.setCreateDate(certificate.getCreateDate());
        dto.setLastUpdateDate(certificate.getLastUpdateDate());
        List<TagDto> dtoList = new ArrayList<>();
        certificate.getTags().forEach(tag -> dtoList.add(tagDtoMapper.changeTagToTagDto(tag)));
        dto.setTagList(dtoList);
        return dto;
    }

}
