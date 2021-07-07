package com.epam.esm.service.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagDto;
import com.epam.esm.service.mapper.TagDtoMapper;

@Component
public class TagDtoMapperImpl implements TagDtoMapper {

    @Override
    public Tag changeTagDtoToTag(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setId(tagDto.getId());
        tag.setNameTag(tagDto.getNameTag());
        return tag;
    }

    @Override
    public TagDto changeTagToTagDto(Tag tag) {
        TagDto dto = new TagDto();
        dto.setId(tag.getId());
        dto.setNameTag(tag.getNameTag());
        return dto;
    }

    @Override
    public List<Tag> changeCertificateDtoToTagList(GiftCertificateDto dto) {
        List<Tag> tagList = new ArrayList<>();
        dto.getTagList().forEach(tagDto -> tagList.add(changeTagDtoToTag(tagDto)));
        return tagList;
    }
}
