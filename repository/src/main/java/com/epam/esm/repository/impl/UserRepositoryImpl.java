package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.List;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    public static final int OFFSET_DEFAULT_VALUE = 1;
    public static final String QUERY_FIND_USER_WITH_HIGHIEST_COST_OF_ORDERS = "select o.user from Order o group by o.user order by sum(o.price) desc";
    private static final int POSITION_WITH_MAX_VALUE = 1;
    private static final String COLUMN_NAME = "name";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAll(int offset, int limit) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> userCriteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = userCriteriaQuery.from(User.class);
        userCriteriaQuery.select(root);
        int itemsOffset = (offset - OFFSET_DEFAULT_VALUE) * limit;
        return entityManager.createQuery(userCriteriaQuery)
                .setFirstResult(itemsOffset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public User findById(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User create(User entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public long delete(long id) {
        entityManager.remove(findById(id));
        return id;
    }

    @Override
    public Long findNumberOfEntities() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        query.select(builder.count(query.from(User.class)));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public Long findUserWithTheHighestCostOfAllOrder() {
        return entityManager.createQuery(QUERY_FIND_USER_WITH_HIGHIEST_COST_OF_ORDERS, User.class)
                .setMaxResults(POSITION_WITH_MAX_VALUE)
                .getSingleResult()
                .getId();
    }

    @Override
    public List<User> findByName(String userName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> giftCertificateCriteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = giftCertificateCriteriaQuery.from(User.class);
        Predicate predicate = criteriaBuilder.like(root.get(COLUMN_NAME), userName);
        giftCertificateCriteriaQuery.select(root).where(predicate);
        return entityManager.createQuery(giftCertificateCriteriaQuery).getResultList();
    }

}
