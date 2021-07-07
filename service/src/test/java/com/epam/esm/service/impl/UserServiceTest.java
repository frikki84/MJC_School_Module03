package com.epam.esm.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.esm.entity.User;
import com.epam.esm.entity.UserDto;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.NoSuchResourceException;
import com.epam.esm.service.mapper.UserDtoMapper;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDtoMapper mapper;
    @InjectMocks
    private UserService userService;

    public static final int OFFSET = 10;
    public static final int LIMIT = 10;
    public static final long ID = 1L;

    private User user = new User();
    private UserDto dto = new UserDto();
    List<User> users = new ArrayList<>();
    List<UserDto> dtos = new ArrayList<>();

    @BeforeEach
    void setUp() {
        users.add(user);
        dtos.add(dto);
    }

    @Test
    void findAll() {
        Mockito.when(userRepository.findAll(OFFSET, LIMIT)).thenReturn(users);
        Mockito.when(mapper.chandeUserToDto(user)).thenReturn(dto);
        assertEquals(dtos, userService.findAll(OFFSET, LIMIT));
    }

    @Test
    void findByIdPositive() {
        Mockito.when(userRepository.findById(ID)).thenReturn(user);
        Mockito.when(mapper.chandeUserToDto(user)).thenReturn(dto);
        assertEquals(dto, userService.findById(ID));
    }

    @Test
    void findByIdNegative() {
        Mockito.when(userRepository.findById(ID)).thenThrow(NoSuchResourceException.class);
        assertThrows(NoSuchResourceException.class, () -> userService.findById(ID));
    }

    @Test
    void createPositive() {
        Mockito.when(userRepository.create(user)).thenReturn(user);
        Mockito.when(mapper.chandeUserToDto(user)).thenReturn(dto);
        assertEquals(dto, userService.create(dto));
    }

    @Test
    void deletePositive() {
        Mockito.when(userRepository.delete(ID)).thenReturn(ID);
        assertEquals(ID, userService.delete(ID));
    }

    @Test
    void deleteNegative() {
        Mockito.when(userRepository.delete(ID)).thenThrow(RuntimeException.class);
        assertThrows(NoSuchResourceException.class, () -> userService.delete(ID));
    }

    @Test
    void findNumberOfEntities() {
        Mockito.when(userRepository.findNumberOfEntities()).thenReturn(ID);
        assertEquals(ID, userService.findNumberOfEntities());
    }
}