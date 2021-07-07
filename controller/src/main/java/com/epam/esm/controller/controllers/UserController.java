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
import com.epam.esm.entity.UserDto;
import com.epam.esm.service.UserService;

@RestController
@RequestMapping("v2/users")
public class UserController {

    public static final String DEFAULTE_PAGE_VALUE = "1";
    public static final String DEFAULTE_SIZE_VALUE = "10";

    @Autowired
    private UserService service;
    private HateoasBuilder hateoasBuilder;
    private final PaginationBuilder<UserDto> paginationBuilder;

    public UserController(UserService service, HateoasBuilder hateoasBuilder, PaginationBuilder<UserDto> pagination) {
        this.service = service;
        this.hateoasBuilder = hateoasBuilder;
        this.paginationBuilder = pagination;
    }

    @GetMapping
    public PagedModel<UserDto> findAll(
            @RequestParam(value = "page", required = false, defaultValue = DEFAULTE_PAGE_VALUE) int page,
            @RequestParam(value = "size", required = false, defaultValue = DEFAULTE_SIZE_VALUE) int size) {
        List<UserDto> list = service.findAll(page, size);
        hateoasBuilder.addLinksToListUser(list);
        return paginationBuilder.addPagination(list, page, size, service.findNumberOfEntities());
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable long id) {
        return hateoasBuilder.addLinksToUser(service.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody UserDto dto) {
        return hateoasBuilder.addLinksToUser(service.create(dto));
    }

    @DeleteMapping("/{id}")
    public Long delete(@PathVariable Long id) {
        return service.delete(id);
    }

}
