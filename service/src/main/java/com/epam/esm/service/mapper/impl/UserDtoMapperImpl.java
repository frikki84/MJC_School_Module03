package com.epam.esm.service.mapper.impl;

import org.springframework.stereotype.Component;

import com.epam.esm.entity.User;
import com.epam.esm.entity.UserDto;
import com.epam.esm.service.mapper.UserDtoMapper;

@Component
public class UserDtoMapperImpl implements UserDtoMapper {

    @Override
    public User chandeDtoToUser(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        return user;
    }

    @Override
    public UserDto chandeUserToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        return dto;
    }
}
