package com.epam.esm.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderCreationParameter;
import com.epam.esm.entity.OrderDto;
import com.epam.esm.entity.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.CustomErrorCode;
import com.epam.esm.service.exception.NoSuchResourceException;
import com.epam.esm.service.mapper.OrderDtoMapper;
import com.epam.esm.service.validation.PageInfoValidation;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderDtoMapper orderMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GiftCertificateRepository certificateRepository;
    @Mock
    private PageInfoValidation pageInfoValidation;
    @InjectMocks
    private OrderService orderService;

    public static final int OFFSET = 10;
    public static final int LIMIT = 10;
    public static final long ID = 1L;

    @Test
    void findAll() {
        Mockito.when(pageInfoValidation.checkPageInfo(OFFSET, LIMIT, CustomErrorCode.TAG)).thenReturn(true);

    }

    @Test
    void findByIdPositive() {
        Order order = new Order();
        OrderDto orderDto = new OrderDto();
        Mockito.when(orderMapper.chandeOrderToDto(order)).thenReturn(orderDto);
        Mockito.when(orderRepository.findById(ID)).thenReturn(order);
        assertEquals(orderDto, orderService.findById(ID));
    }

    @Test
    void findByIdNegative() {
        Mockito.when(orderRepository.findById(ID)).thenThrow(NoSuchResourceException.class);
        assertThrows(NoSuchResourceException.class, () -> orderService.findById(ID));
    }

    @Test
    void create() {
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        OrderCreationParameter parameter = new OrderCreationParameter();
        parameter.setUserId(ID);
        parameter.setCertificateDtos(Arrays.asList(1));
        Order order = new Order();
        OrderDto orderDto = new OrderDto();
        User user = new User();
        GiftCertificate certificate = new GiftCertificate();
        certificate.setPrice(new BigDecimal(1));
        Mockito.when(userRepository.findById(ID)).thenReturn(user);
        Mockito.when(certificateRepository.findById(ID)).thenReturn(certificate);
        Mockito.when(orderMapper.chandeOrderToDto(orderRepository.create(order))).thenReturn(orderDto);

        assertEquals(orderDto, orderService.create(parameter));
    }

    @Test
    void createNegativeNoUser() {
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        OrderCreationParameter parameter = new OrderCreationParameter();
        parameter.setUserId(ID);
        parameter.setCertificateDtos(Arrays.asList(1));
        Order order = new Order();
        OrderDto orderDto = new OrderDto();
        User user = new User();
        GiftCertificate certificate = new GiftCertificate();
        certificate.setPrice(new BigDecimal(1));
        Mockito.when(userRepository.findById(ID)).thenReturn(null);
        assertThrows(NoSuchResourceException.class, () -> orderService.create(parameter));
    }

    @Test
    void createNegativeNoCertificate() {
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        OrderCreationParameter parameter = new OrderCreationParameter();
        parameter.setUserId(ID);
        parameter.setCertificateDtos(Arrays.asList(1));
        Order order = new Order();
        OrderDto orderDto = new OrderDto();
        User user = new User();
        GiftCertificate certificate = new GiftCertificate();
        certificate.setPrice(new BigDecimal(1));
        Mockito.when(userRepository.findById(ID)).thenReturn(user);
        Mockito.when(certificateRepository.findById(ID)).thenReturn(null);
        assertThrows(NoSuchResourceException.class, () -> orderService.create(parameter));
    }

    @Test
    void deletePositive() {
        Mockito.when(orderRepository.delete(ID)).thenReturn(ID);
        assertEquals(ID, orderService.delete(ID));
    }

    @Test
    void deleteNegative() {
        Mockito.when(orderRepository.delete(ID)).thenThrow(RuntimeException.class);
        assertThrows(NoSuchResourceException.class, () -> orderService.delete(ID));
    }

    @Test
    void findNumberOfEntities() {
        Mockito.when(orderRepository.findNumberOfEntities()).thenReturn(ID);
        assertEquals(ID, orderService.findNumberOfEntities());
    }

    @Test
    void readOrdersByUserPositive() {
        Order order = new Order();
        OrderDto orderDto = new OrderDto();
        List<Order> orderList = Arrays.asList(order);
        List<OrderDto> dtoList = Arrays.asList(orderDto);
        Mockito.when(orderRepository.readOrdersByUser(ID)).thenReturn(orderList);
        Mockito.when(orderMapper.chandeOrderToDto(order)).thenReturn(orderDto);
        assertEquals(dtoList, orderService.readOrdersByUser(ID));

    }

    @Test
    void readOrdersByUserNegative() {
        Mockito.when(orderRepository.readOrdersByUser(ID)).thenReturn(new ArrayList<Order>());
        assertThrows(NoSuchResourceException.class, () -> orderService.readOrdersByUser(ID));

    }
}