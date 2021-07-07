package com.epam.esm.service.mapper;

import com.epam.esm.entity.User;
import com.epam.esm.entity.UserDto;

public interface UserDtoMapper {

    public User chandeDtoToUser(UserDto dto);

    public UserDto chandeUserToDto(User user);

}
