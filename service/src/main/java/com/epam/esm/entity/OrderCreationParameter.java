package com.epam.esm.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationParameter {

    private long userId;
    private List<Integer> certificateDtos;
}
