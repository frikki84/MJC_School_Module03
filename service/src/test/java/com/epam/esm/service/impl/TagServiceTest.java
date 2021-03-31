package com.epam.esm.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagDto;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.NoSuchResourceException;
import com.epam.esm.service.exception.TagValidationException;
import com.epam.esm.service.mapper.TagDtoMapper;
import com.epam.esm.service.validation.TagValidation;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    public static final int OFFSET = 10;
    public static final int LIMIT = 10;
    public static final long ID = 1L;
    public static final String TAG_NAME = "Entity";

    @Mock
    private TagRepository tagRepository;
    @Mock
    private TagDtoMapper mapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TagValidation tagValidation;

    @InjectMocks
    private TagService tagService;

    @Test
    void findAll() {

        Tag tag = new Tag();
        List<Tag> tags = Arrays.asList(tag);
        TagDto dto = new TagDto();
        List<TagDto> expected = Arrays.asList(dto);
        Mockito.when(tagRepository.findAll(OFFSET, LIMIT)).thenReturn(tags);
        Mockito.when(mapper.changeTagToTagDto(tag)).thenReturn(dto);
        assertEquals(expected, tagService.findAll(OFFSET, LIMIT));
    }

    @Test
    void findByIdPositive() {
        Tag tag = new Tag();
        TagDto dto = new TagDto();
        Mockito.when(tagRepository.findById(ID)).thenReturn(tag);
        Mockito.when(mapper.changeTagToTagDto(tag)).thenReturn(dto);
        assertEquals(dto, tagService.findById(ID));
    }

    @Test
    void findByIdNegative() {
        Mockito.when(tagRepository.findById(ID)).thenThrow(NoSuchResourceException.class);
        assertThrows(NoSuchResourceException.class, () -> tagService.findById(ID));
    }

    @Test
    void createPositive() {
        Tag tag = new Tag();
        TagDto dto = new TagDto();
        dto.setNameTag(TAG_NAME);
         Mockito.when(tagValidation.chechTagDtoFormat(dto)).thenReturn(true);
        Mockito.when(tagRepository.create(tag)).thenReturn(tag);
        Mockito.when(mapper.changeTagToTagDto(tag)).thenReturn(dto);
        assertEquals(dto, tagService.create(dto));
    }

    @Test
    void createNegative() {
        TagDto dto = new TagDto();
        Mockito.when(tagValidation.chechTagDtoFormat(dto)).thenThrow(TagValidationException.class);
        assertThrows(TagValidationException.class, () -> tagService.create(dto));
    }

    @Test
    void deletePositive() {
        Mockito.when(tagRepository.delete(ID)).thenReturn(ID);
        assertEquals(ID, tagService.delete(ID));
    }

    @Test
    void deleteNegative() {
        Mockito.when(tagRepository.delete(ID)).thenThrow(NoSuchResourceException.class);
        assertThrows(NoSuchResourceException.class, () -> tagService.delete(ID));
    }

    @Test
    void findNumberOfEntities() {
        Mockito.when(tagRepository.findNumberOfEntities()).thenReturn(ID);
        assertEquals(ID, tagService.findNumberOfEntities());
    }

    @Test
    void findMostWidelyUsedTagOfUserWithTheHighestCostOfAllOrder() {
        Tag tag = new Tag();
        TagDto dto = new TagDto();
        Mockito.when(userRepository.findUserWithTheHighestCostOfAllOrder()).thenReturn(ID);
        Mockito.when(tagRepository.getMostWidelyUsedUsersTag(ID)).thenReturn(tag);
        Mockito.when(mapper.changeTagToTagDto(tag)).thenReturn(dto);
        assertEquals(ID, tagService.findMostWidelyUsedTagOfUserWithTheHighestCostOfAllOrder());

    }
}