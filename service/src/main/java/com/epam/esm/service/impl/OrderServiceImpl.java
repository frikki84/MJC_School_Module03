package com.epam.esm.service.impl;

import com.epam.esm.entity.*;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.CustomErrorCode;
import com.epam.esm.service.exception.NoSuchResourceException;
import com.epam.esm.service.mapper.OrderDtoMapper;
import com.epam.esm.service.validation.PageInfoValidation;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private final OrderRepository orderRepository;
    private final OrderDtoMapper orderMapper;
    private final UserRepository userRepository;
    private final GiftCertificateRepository certificateRepository;
    private final PageInfoValidation pageInfoValidation;

    public OrderServiceImpl(OrderRepository orderRepository, OrderDtoMapper orderMapper, UserRepository userRepository, GiftCertificateRepository certificateRepository, PageInfoValidation pageInfoValidation) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userRepository = userRepository;
        this.certificateRepository = certificateRepository;
        this.pageInfoValidation = pageInfoValidation;
    }

    @Override
    public List<OrderDto> findAll(int offset, int limit) {
        pageInfoValidation.checkPageInfo(offset, limit);
        return orderRepository.findAll(offset, limit).stream()
                .map(order -> orderMapper.chandeOrderToDto(order))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto findById(long id) {
        Order order = orderRepository.findById(id);
        if (Objects.isNull(order)) {
            throw new NoSuchResourceException(CustomErrorCode.ORDER);
        }
        return orderMapper.chandeOrderToDto(order);
    }

    @Override
    @Transactional
    public OrderDto create(OrderCreationParameter parameter) {
        User user = userRepository.findById(parameter.getUserId());
        if (Objects.isNull(user)) {
            throw new NoSuchResourceException(CustomErrorCode.USER);
        }
        List<GiftCertificate> certificatesList = new ArrayList<>();
        BigDecimal price = new BigDecimal(0);
        for (Integer i : parameter.getCertificateDtos()) {
            GiftCertificate certificate = certificateRepository.findById(i);
            if (Objects.isNull(certificate)) {
                throw new NoSuchResourceException(CustomErrorCode.ORDER);
            }
            certificatesList.add(certificate);
            price.add(certificate.getPrice());
        }
        Order order = new Order();
        order.setUser(user);
        order.setPrice(price);
        order.setDate(LocalDateTime.now());
        order.setGiftCertificateList(certificatesList);
        return orderMapper.chandeOrderToDto(orderRepository.create(order));
    }

    @Override
    public long delete(long id) {
        Long findId = null;
        try {
            findId=orderRepository.delete(id);
        } catch (RuntimeException e) {
            throw new NoSuchResourceException(CustomErrorCode.ORDER);
        }
        return findId;
    }

    @Override
    public long findNumberOfEntities() {
        return orderRepository.findNumberOfEntities();
    }

    @Override
    public List<OrderDto> readOrdersByUser(long userId) {
        return orderRepository.readOrdersByUser(userId).stream().map(order -> orderMapper.chandeOrderToDto(order))
                .collect(Collectors.toList());
    }
}
