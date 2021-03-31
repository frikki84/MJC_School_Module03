package com.epam.esm.service.validation;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.epam.esm.entity.User;
import com.epam.esm.entity.UserDto;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.exception.UserValidationException;

@Component
public class UserDtoValidation {

    public static final boolean DEFAULT_RESULT = true;

    public static final String INVALID_USER_NAME = "userdto_invalid_name";
    public static final String USER_NAME_EXISTS = "username_exists";
    public static final int MIN_USER_LENGTH = 3;
    public static final int MAX_USER_LENGTH = 32;

    private final UserRepository userRepository;

    public UserDtoValidation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean checkUserDto(UserDto userDto) {
        boolean result = true;
        chechUserDtoFormat(userDto);
        List<User> userList = userRepository.findByName(userDto.getName());
        if (Objects.nonNull(userList) && !userList.isEmpty()) {
            throw new UserValidationException(USER_NAME_EXISTS);
        }
        return result;
    }

    private boolean chechUserDtoFormat(UserDto dto) {
        boolean result = DEFAULT_RESULT;
        if (dto.getName() == null || dto.getName().isBlank() || dto.getName().length() < MIN_USER_LENGTH
                || dto.getName().length() > MAX_USER_LENGTH) {
            throw new UserValidationException(INVALID_USER_NAME);
        }
        return result;
    }

}
