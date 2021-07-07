package com.epam.esm.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.entity.SearchGiftCertificateParameter;
import com.epam.esm.entity.SearchGiftCertificateParameterDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagDto;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.exception.CustomErrorCode;
import com.epam.esm.service.exception.NoSuchResourceException;
import com.epam.esm.service.mapper.CertificateDtoMapper;
import com.epam.esm.service.mapper.SearchParamterDtoMapper;
import com.epam.esm.service.mapper.TagDtoMapper;
import com.epam.esm.service.validation.GiftCertificateDtoValidation;
import com.epam.esm.service.validation.PageInfoValidation;

@Service
@Transactional
public class GiftCertificateService implements CrdService<GiftCertificateDto> {

    @Autowired
    private final GiftCertificateRepository giftCertificateRepository;
    private final CertificateDtoMapper mapper;
    private final PageInfoValidation pageValidation;
    private final TagRepository tagRepository;
    private final SearchParamterDtoMapper paramterDtoMapper;
    private final TagDtoMapper tagDtoMapper;
    private final GiftCertificateDtoValidation certificateValidation;



    public GiftCertificateService(GiftCertificateRepository giftCertificateRepository, CertificateDtoMapper mapper,
            PageInfoValidation pageValidation, TagRepository tagRepository, SearchParamterDtoMapper paramterDtoMapper,
            TagDtoMapper tagDtoMapper, GiftCertificateDtoValidation certificateValidation) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.mapper = mapper;
        this.pageValidation = pageValidation;
        pageValidation.setCrdOperations(giftCertificateRepository);
        this.tagRepository = tagRepository;
        this.paramterDtoMapper = paramterDtoMapper;
        this.tagDtoMapper = tagDtoMapper;
        this.certificateValidation = certificateValidation;

    }

    @Override
    public List<GiftCertificateDto> findAll(int offset, int limit) {
        pageValidation.checkPageInfo(offset, limit, CustomErrorCode.CERTIFICATE);
        return giftCertificateRepository.findAll(offset, limit)
                .stream()
                .map(mapper::changeCertificateToDto)
                .collect(Collectors.toList());
    }

    public List<GiftCertificateDto> findAll(SearchGiftCertificateParameterDto parametrDto, int offset, int limit) {
        pageValidation.checkPageInfo(offset, limit, CustomErrorCode.CERTIFICATE);
        if (Objects.isNull(parametrDto) || parametrDto.isEmpty()) {
            return findAll(offset, limit);
        }
        SearchGiftCertificateParameter parametr = paramterDtoMapper.changeDtoToSearchGiftSertificateParametr(
                parametrDto);
        return giftCertificateRepository.findAll(parametr, offset, limit)
                .stream()
                .map(mapper::changeCertificateToDto)
                .collect(Collectors.toList());
    }


    public long findNumberOfEntities(SearchGiftCertificateParameterDto parametrDto) {
        SearchGiftCertificateParameter parametr = paramterDtoMapper.changeDtoToSearchGiftSertificateParametr(
                parametrDto);
        return giftCertificateRepository.getCountOfEntities(parametr);
    }


    public long findNumberOfEntities() {
        return giftCertificateRepository.findNumberOfEntities();
    }

    @Override
    public GiftCertificateDto findById(long id) {
        GiftCertificate certificate = giftCertificateRepository.findById(id);
        if (Objects.isNull(certificate)) {
            throw new NoSuchResourceException(CustomErrorCode.CERTIFICATE);
        }
        return mapper.changeCertificateToDto(certificate);
    }

    @Override
    public GiftCertificateDto create(GiftCertificateDto entity) {
        GiftCertificateDtoValidation.chechCertificateDtoFormat(entity);
        GiftCertificate certificate = mapper.changeDtoToCertificate(entity);
        LocalDateTime currentDate = LocalDateTime.now();
        certificate.setCreateDate(currentDate);
        certificate.setLastUpdateDate(currentDate);
        List<Tag> tags = correctTagList(entity.getTagList());
        certificate.setTags(tags);
        GiftCertificate createdCertificate = giftCertificateRepository.create(certificate);
        return mapper.changeCertificateToDto(createdCertificate);

    }

    @Override
    public long delete(long id) {
        long findId;
        try {
            findId = giftCertificateRepository.delete(id);
        } catch (RuntimeException e) {
            throw new NoSuchResourceException(CustomErrorCode.CERTIFICATE);
        }
        return findId;
    }

    @Transactional
    public GiftCertificateDto update(GiftCertificateDto giftCertificate, Long id) {
        GiftCertificate certificateFromDb = giftCertificateRepository.findById(id);

        if (Objects.isNull(certificateFromDb)) {
            throw new NoSuchResourceException(CustomErrorCode.CERTIFICATE);
        }
        boolean checkDto = certificateValidation.chechCertificateDtoFormatForUpdate(giftCertificate);

        GiftCertificate certificate = mapper.changeDtoToCertificate(giftCertificate);
        if (Objects.nonNull(certificate.getName())) {
            certificateFromDb.setName(certificate.getName());
        }
        if (Objects.nonNull(certificate.getDescription())) {
            certificateFromDb.setDescription(certificate.getDescription());
        }
        if (Objects.nonNull(certificate.getPrice())) {
            certificateFromDb.setPrice(certificate.getPrice());
        }
        if (Objects.nonNull(certificate.getDuration())) {
            certificateFromDb.setDuration(certificate.getDuration());
        }
        if (Objects.nonNull(certificate.getCreateDate())) {
            certificateFromDb.setCreateDate(certificate.getCreateDate());
        }

        if (Objects.nonNull(certificate.getTags())) {
            List<Tag> newTagList = createTagListWithAdditionalTags(certificateFromDb, certificate);
            certificateFromDb.setTags(newTagList);
        }
        LocalDateTime currentDate = LocalDateTime.now();
        certificateFromDb.setLastUpdateDate(currentDate);
        return mapper.changeCertificateToDto(giftCertificateRepository.update(certificateFromDb));
    }

    private List<Tag> correctTagList(List<TagDto> tagDtoList) {
        return tagDtoList.stream()
                .map(dto -> checkTagOperation(tagDtoMapper.changeTagDtoToTag(dto)))
                .collect(Collectors.toList());
    }

    private Tag checkTagOperation(Tag tag) {
        try {
            return tagRepository.findByName(tag.getNameTag()).get(0);
        } catch (RuntimeException e) {
            return tagRepository.create(tag);
        }
    }

    private List<Tag> createTagListWithAdditionalTags(GiftCertificate giftCertificateFromDb,
            GiftCertificate updatedGiftCertificate) {
        List<Tag> newTags = updatedGiftCertificate.getTags();
        List<Tag> correctList = newTags.stream().map(tag -> checkTagOperation(tag)).collect(Collectors.toList());
        for (Tag tag : giftCertificateFromDb.getTags()) {
            if (!correctList.contains(tag)) {
                correctList.add(tag);
            }
        }

        return correctList;
    }

}
