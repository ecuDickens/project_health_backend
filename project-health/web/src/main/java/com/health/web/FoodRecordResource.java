package com.health.web;

import com.google.inject.Inject;
import com.health.base.ThrowingFunction1;
import com.health.collect.MoreCollections;
import com.health.entity.*;
import com.health.exception.HttpException;
import com.health.helper.JpaHelper;
import com.health.types.ErrorType;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.util.List;

import static com.health.helper.JpaHelper.buildResponse;
import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;
import static javax.ws.rs.core.Response.Status.*;

public class FoodRecordResource {

    private final JpaHelper jpaHelper;

    @Inject
    public FoodRecordResource(JpaHelper jpaHelper) {
        this.jpaHelper = jpaHelper;
    }

    public Response createFoodRecord(final Long accountId, final FoodRecord record) throws HttpException {
        if (!jpaHelper.isLoggedIn(accountId)) {
            return buildResponse(UNAUTHORIZED, new ErrorType("Login expired, please login again."));
        }

        final Long recordId = jpaHelper.executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
            @Override
            public Long apply(EntityManager em) throws HttpException {
                record.setAccount(em.getReference(Account.class, accountId));
                record.setRecipe(em.getReference(Recipe.class, record.getRecipeId()));
                em.persist(record);
                em.flush();
                return record.getId();
            }
        });

        if (null == recordId) {
            return buildResponse(BAD_REQUEST, new ErrorType("Unable to create record."));
        }

        return buildResponse(OK, new Recipe().withId(recordId));
    }

    public Response loadFoodRecords(final Long accountId, final Date startDate, final Date endDate) throws HttpException {
        if (!jpaHelper.isLoggedIn(accountId)) {
            return buildResponse(UNAUTHORIZED, new ErrorType("Login expired, please login again."));
        }

        final List<FoodRecord> records = jpaHelper.executeJpa(new ThrowingFunction1<List<FoodRecord>, EntityManager, HttpException>() {
            @Override
            public List<FoodRecord> apply(EntityManager em) throws HttpException {
                final String queryStr = String.format("select a from FoodRecord a where a.accountId = :accountId%s%s",
                        null != startDate ? " and a.recordDate >= :startDate" : "",
                        null != endDate ? " and a.recordDate <= :endDate" : "");
                final TypedQuery<FoodRecord> query = em.createQuery(queryStr, FoodRecord.class);
                query.setParameter("accountId", accountId);
                if (null != startDate) {
                    query.setParameter("startDate", startDate);
                }
                if (null != endDate) {
                    query.setParameter("endDate", endDate);
                }
                return query.getResultList();
            }
        });
        if (MoreCollections.isNullOrEmpty(records)) {
            return buildResponse(NO_CONTENT, new ErrorType("Records not found"));
        }
        for (FoodRecord record : records) {
            record.clean();
        }
        return buildResponse(OK, records);
    }

    public Response loadFoodRecord(final Long accountId, final Long recordId) throws HttpException {
        if (!jpaHelper.isLoggedIn(accountId)) {
            return buildResponse(UNAUTHORIZED, new ErrorType("Login expired, please login again."));
        }

        final FoodRecord record = jpaHelper.executeJpa(new ThrowingFunction1<FoodRecord, EntityManager, HttpException>() {
            @Override
            public FoodRecord apply(EntityManager em) throws HttpException {
                return em.find(FoodRecord.class, recordId);
            }
        });
        if (null == record) {
            return buildResponse(NO_CONTENT, new ErrorType("Record not found"));
        }
        record.clean();
        return buildResponse(OK, record);
    }

    public Response updateFoodRecord(final Long accountId, final Long recordId, final FoodRecord record) throws HttpException {
        if (!jpaHelper.isLoggedIn(accountId)) {
            return buildResponse(UNAUTHORIZED, new ErrorType("Login expired, please login again."));
        }

        jpaHelper.executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
            @Override
            public Long apply(EntityManager em) throws HttpException {
                final FoodRecord forUpdate = em.find(FoodRecord.class, recordId);
                em.refresh(forUpdate, PESSIMISTIC_WRITE);
                forUpdate
                        .withRecipe(null == record.getRecipeId() ? forUpdate.getRecipe() : em.getReference(Recipe.class, record.getRecipeId()))
                        .withCaloriesConsumed(null == record.getCaloriesConsumed() ? forUpdate.getCaloriesConsumed() : record.getCaloriesConsumed())
                        .withRecordDate(null == record.getRecordDate() ? forUpdate.getRecordDate() : record.getRecordDate());
                return forUpdate.getId();
            }
        });
        return buildResponse(OK);
    }

    public Response deleteFoodRecord(final Long accountId, final Long recordId) throws HttpException {
        if (!jpaHelper.isLoggedIn(accountId)) {
            return buildResponse(UNAUTHORIZED, new ErrorType("Login expired, please login again."));
        }

        jpaHelper.executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
            @Override
            public Long apply(EntityManager em) throws HttpException {
                em.remove(em.find(FoodRecord.class, recordId));
                return null;
            }
        });
        return buildResponse(NO_CONTENT);
    }
}
