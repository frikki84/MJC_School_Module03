package com.epam.esm.service.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderDto;
import com.epam.esm.service.mapper.CertificateDtoMapper;
import com.epam.esm.service.mapper.OrderDtoMapper;
import com.epam.esm.service.mapper.UserDtoMapper;

@Component
public class OrderDtoMapperImpl implements OrderDtoMapper {

    private CertificateDtoMapper mapper;
    private UserDtoMapper userDtoMapper;

    public OrderDtoMapperImpl() {
    }

    @Autowired
    public void setMapper(CertificateDtoMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setUserDtoMapper(UserDtoMapper userDtoMapper) {
        this.userDtoMapper = userDtoMapper;
    }

    @Override
    public Order chandeDtoToOrder(OrderDto dto) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setPrice(dto.getPrice());
        order.setUser(userDtoMapper.chandeDtoToUser(dto.getUser()));
        order.setDate(dto.getDate());
        List<GiftCertificate> dtoList = new ArrayList<>();
        if (Objects.nonNull(dto.getGiftCertificateList())) {
            dto.getGiftCertificateList()
                    .forEach(certificateDto -> dtoList.add(mapper.changeDtoToCertificate(certificateDto)));
        }
        order.setGiftCertificateList(dtoList);
        return order;
    }

    @Override
    public OrderDto chandeOrderToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setPrice(order.getPrice());
        dto.setDate(order.getDate());
        dto.setUser(userDtoMapper.chandeUserToDto(order.getUser()));
        List<GiftCertificateDto> certificateDtos = new ArrayList<>();
        order.getGiftCertificateList()
                .forEach(giftCertificate -> certificateDtos.add(mapper.changeCertificateToDto(giftCertificate)));
        dto.setGiftCertificateList(certificateDtos);
        return dto;
    }
}
