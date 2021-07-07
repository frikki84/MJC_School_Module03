package com.epam.esm.controller.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.controller.util.HateoasBuilder;
import com.epam.esm.controller.util.PaginationBuilder;
import com.epam.esm.entity.OrderCreationParameter;
import com.epam.esm.entity.OrderDto;
import com.epam.esm.service.OrderService;

@RestController
@RequestMapping("/v2/orders")
public class OrderController {

    public static final String DEFAULT_PAGE_VALUE = "1";
    public static final String DEFAULT_SIZE_VALUE = "10";

    @Autowired
    private final OrderService orderService;
    private final HateoasBuilder hateoasBuilder;
    private final PaginationBuilder<OrderDto> paginationBuilder;

    public OrderController(OrderService orderService, HateoasBuilder hateoas, PaginationBuilder<OrderDto> pagination) {
        this.orderService = orderService;
        this.hateoasBuilder = hateoas;
        this.paginationBuilder = pagination;
    }

    @GetMapping
    public PagedModel<OrderDto> findAll(
            @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE_VALUE) int page,
            @RequestParam(value = "size", required = false, defaultValue = DEFAULT_SIZE_VALUE) int size) {
        List<OrderDto> list = orderService.findAll(page, size);
        hateoasBuilder.addLinksToListOrder(list);
        return paginationBuilder.addPagination(list, page, size, orderService.findNumberOfEntities());
    }

    @GetMapping("/{id}")
    public OrderDto findById(@PathVariable long id) {
        return hateoasBuilder.addLinksToOrder(orderService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@RequestBody OrderCreationParameter parameter) {
        return hateoasBuilder.addLinksToOrder(orderService.create(parameter));
    }

    @DeleteMapping("/{id}")
    public long delete(@PathVariable long id) {
        return orderService.delete(id);
    }

    @GetMapping(params = "user")
    public List<OrderDto> readOrdersByUser(@RequestParam(value = "user", required = true) long userId) {
        List<OrderDto> orderDtoList = orderService.readOrdersByUser(userId);
        hateoasBuilder.addLinksToListOrder(orderDtoList);
        return orderDtoList;
    }
}
