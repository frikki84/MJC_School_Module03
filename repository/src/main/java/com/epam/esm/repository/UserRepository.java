package com.epam.esm.repository;

import java.util.List;

import com.epam.esm.entity.User;

public interface UserRepository extends CrdOperations<User> {

    public Long findUserWithTheHighestCostOfAllOrder();
    public List<User> findByName(String userName);

}
