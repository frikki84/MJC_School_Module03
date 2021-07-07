package com.epam.esm.repository;

import java.util.List;

import com.epam.esm.entity.Order;

public interface OrderRepository extends CrdOperations<Order> {

    List<Order> readOrdersByUser(long userID);
}
