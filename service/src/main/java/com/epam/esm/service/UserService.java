package com.epam.esm.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.entity.User;
import com.epam.esm.entity.UserDto;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.exception.CustomErrorCode;
import com.epam.esm.service.exception.NoSuchResourceException;
import com.epam.esm.service.mapper.UserDtoMapper;
import com.epam.esm.service.validation.PageInfoValidation;
import com.epam.esm.service.validation.UserDtoValidation;

@Service
@Transactional
public class UserService implements CrdService<UserDto> {

    private final UserRepository userRepository;
    private final UserDtoMapper mapper;
    private final PageInfoValidation pageValidation;
    private final UserDtoValidation userDtoValidation;

    public UserService(UserRepository userRepository, UserDtoMapper mapper, PageInfoValidation pageValidation,
            UserDtoValidation userDtoValidation) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.pageValidation = pageValidation;
        this.userDtoValidation = userDtoValidation;
        pageValidation.setCrdOperations(userRepository);
    }

    @Override
    public List<UserDto> findAll(int offset, int limit) {
        pageValidation.checkPageInfo(offset, limit, CustomErrorCode.USER);
        return userRepository.findAll(offset, limit)
                .stream()
                .map(user -> mapper.chandeUserToDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(long id) {
        User user = userRepository.findById(id);
        if (Objects.isNull(user)) {
            throw new NoSuchResourceException(CustomErrorCode.USER);
        }
        return mapper.chandeUserToDto(user);
    }

    @Override
    public UserDto create(UserDto entity) {
        userDtoValidation.checkUserDto(entity);
        return mapper.chandeUserToDto(userRepository.create(mapper.chandeDtoToUser(entity)));
    }

    @Override
    public long delete(long id) {
        Long findId;
        try {
            findId = userRepository.delete(id);
        } catch (RuntimeException e) {
            throw new NoSuchResourceException(CustomErrorCode.USER);
        }
        return findId;
    }

    @Override
    public long findNumberOfEntities() {
        return userRepository.findNumberOfEntities();
    }
}
