package com.epam.esm.service.mapper;

import java.util.List;

import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagDto;

public interface TagDtoMapper {

    public Tag changeTagDtoToTag(TagDto tagDto);

    public TagDto changeTagToTagDto(Tag tag);

    public List<Tag> changeCertificateDtoToTagList(GiftCertificateDto dto);

}
