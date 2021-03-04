package com.epam.esm.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Relation(itemRelation = "order", collectionRelation = "orders")
public class OrderDto extends RepresentationModel<OrderDto> {
    private long id;
    private BigDecimal price;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime date;
    @JsonIgnoreProperties(value = {"orderList"})
    private UserDto user;
    @JsonIgnoreProperties(value = {"description", "duration", "createDate", "lastUpdateDate", "tags"})
    private List<GiftCertificateDto> giftCertificateList;

}