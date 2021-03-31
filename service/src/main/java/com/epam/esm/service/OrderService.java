package com.epam.esm.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderCreationParameter;
import com.epam.esm.entity.OrderDto;
import com.epam.esm.entity.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.exception.CustomErrorCode;
import com.epam.esm.service.exception.NoSuchResourceException;
import com.epam.esm.service.mapper.OrderDtoMapper;
import com.epam.esm.service.validation.PageInfoValidation;

@Service
@Transactional
public class OrderService {

    @Autowired
    private final OrderRepository orderRepository;
    private final OrderDtoMapper orderMapper;
    private final UserRepository userRepository;
    private final GiftCertificateRepository certificateRepository;
    private final PageInfoValidation pageInfoValidation;

    public OrderService(OrderRepository orderRepository, OrderDtoMapper orderMapper, UserRepository userRepository,
            GiftCertificateRepository certificateRepository, PageInfoValidation pageInfoValidation) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userRepository = userRepository;
        this.certificateRepository = certificateRepository;
        this.pageInfoValidation = pageInfoValidation;
        pageInfoValidation.setCrdOperations(orderRepository);
    }

    public List<OrderDto> findAll(int offset, int limit) {
        pageInfoValidation.checkPageInfo(offset, limit, CustomErrorCode.ORDER);
        return orderRepository.findAll(offset, limit)
                .stream()
                .map(order -> orderMapper.chandeOrderToDto(order))
                .collect(Collectors.toList());
    }

    public OrderDto findById(long id) {
        Order order = orderRepository.findById(id);
        if (Objects.isNull(order)) {
            throw new NoSuchResourceException(CustomErrorCode.ORDER);
        }
        return orderMapper.chandeOrderToDto(order);
    }

    @Transactional
    public OrderDto create(OrderCreationParameter parameter) {
        User user = userRepository.findById(parameter.getUserId());
        if (Objects.isNull(user)) {
            throw new NoSuchResourceException(CustomErrorCode.USER);
        }
        List<GiftCertificate> certificatesList = new ArrayList<>();
        BigDecimal price = BigDecimal.ZERO;
        for (Integer i : parameter.getCertificateDtos()) {
            GiftCertificate certificate = certificateRepository.findById(i);
            if (Objects.isNull(certificate)) {
                throw new NoSuchResourceException(CustomErrorCode.ORDER);
            }
            price = price.add(certificate.getPrice());
            certificatesList.add(certificate);
        }
        Order order = new Order();
        order.setUser(user);
        order.setPrice(price);
        order.setDate(LocalDateTime.now());
        order.setGiftCertificateList(certificatesList);
        return orderMapper.chandeOrderToDto(orderRepository.create(order));
    }

    public long delete(long id) {
        Long findId;
        try {
            findId = orderRepository.delete(id);
        } catch (RuntimeException e) {
            throw new NoSuchResourceException(CustomErrorCode.ORDER);
        }
        return findId;
    }

    public long findNumberOfEntities() {
        return orderRepository.findNumberOfEntities();
    }

    public List<OrderDto> readOrdersByUser(long userId) {
        List<Order> orderList = orderRepository.readOrdersByUser(userId);
        if (Objects.isNull(orderList) || orderList.isEmpty()) {
            throw new NoSuchResourceException(CustomErrorCode.USER);
        }
        return orderList.stream().map(order -> orderMapper.chandeOrderToDto(order)).collect(Collectors.toList());
    }
}
