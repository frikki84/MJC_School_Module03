package com.epam.esm.repository.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.repository.OrderRepository;

@Repository
@Transactional
public class OrderRepositoryImpl implements OrderRepository {

    public static final int OFFSET_DEFAULT_VALUE = 1;
    public static final String PARAMETER_NAME_FOR_FINDING_ORDERS_BY_USER = "user";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Order> findAll(int offset, int limit) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> orderCriteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = orderCriteriaQuery.from(Order.class);
        orderCriteriaQuery.select(root);
        int itemsOffset = (offset - OFFSET_DEFAULT_VALUE) * limit;
        return entityManager.createQuery(orderCriteriaQuery)
                .setFirstResult(itemsOffset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public Order findById(long id) {
        return entityManager.find(Order.class, id);
    }

    @Override
    public Order create(Order entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public long delete(long id) {
        //entityManager.createNativeQuery(DELETE_ORDER_FROM_CERTIFICATES).setParameter(1, id).executeUpdate();
        entityManager.remove(findById(id));
        return id;
    }

    @Override
    public Long findNumberOfEntities() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        query.select(builder.count(query.from(Order.class)));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public List<Order> readOrdersByUser(long userID) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> orderQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = orderQuery.from(Order.class);
        orderQuery.select(root)
                .where(criteriaBuilder.equal(root.get(PARAMETER_NAME_FOR_FINDING_ORDERS_BY_USER), userID));
        return entityManager.createQuery(orderQuery).getResultList();
    }

}
