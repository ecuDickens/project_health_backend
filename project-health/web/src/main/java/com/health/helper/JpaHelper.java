package com.health.helper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.health.base.ThrowingFunction1;
import com.health.entity.Account;
import com.health.entity.HashKey;
import com.health.exception.HttpException;
import com.health.jpa.session.EntitySession;
import com.health.jpa.spi.JpaEntityManagerService;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

import javax.persistence.*;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.sql.Timestamp;

import static com.health.collect.MoreIterables.asFluent;

@Singleton
public class JpaHelper {

    private JpaEntityManagerService jpaManagerService;

    @Inject
    public JpaHelper(JpaEntityManagerService jpaManagerService) {
        this.jpaManagerService = jpaManagerService;
    }

    public <T> T executeJpa(final ThrowingFunction1<T, EntityManager, HttpException> jpaFunction) throws HttpException {
        try {
            return jpaManagerService.invoke(new EntitySession<T>() {
                @Override
                public T execute(EntityManager entityManager) throws HttpException {
                    return jpaFunction.apply(entityManager);
                }
            });
        } catch (NoResultException ex) {
            return null;
        }
    }

    public <T> T executeJpaTransaction(final ThrowingFunction1<T, EntityManager, HttpException> jpaFunction) throws HttpException {
        return jpaManagerService.invoke(new EntitySession<T>() {
            @Override
            public T execute(EntityManager entityManager) throws HttpException {
                boolean rollback = false;
                T result = null;
                try {
                    entityManager.getTransaction().begin();
                    result = jpaFunction.apply(entityManager);
                    entityManager.getTransaction().commit();
                }  catch (EntityNotFoundException e) {
                    rollback = true;
                    throw new HttpException(HttpURLConnection.HTTP_INTERNAL_ERROR, String.format("Record was not found. %s", e.getMessage()), e);
                } catch (NoResultException e) {
                    rollback = true;
                    throw new HttpException(HttpURLConnection.HTTP_INTERNAL_ERROR, String.format("Record was not found. %s", e.getMessage()), e);
                } catch (EntityExistsException t) {
                    rollback = true;
                    throw new HttpException(HttpURLConnection.HTTP_INTERNAL_ERROR, String.format("Record already created. %s", t.getMessage()), t);
                } catch (Throwable t) {
                    rollback = true;
                    throw new HttpException(HttpURLConnection.HTTP_INTERNAL_ERROR, String.format("Transaction error. %s", t.getMessage()), t);
                } finally {
                    if (rollback && entityManager.getTransaction().isActive()) {
                        entityManager.getTransaction().rollback();
                    }
                }
                return result;
            }
        });
    }


    public Boolean isLoggedIn(final Long accountId) throws HttpException {
        return executeJpaTransaction(new ThrowingFunction1<Boolean, EntityManager, HttpException>() {
            @Override
            public Boolean apply(EntityManager em) throws HttpException {
                Boolean isLoggedIn = Boolean.FALSE;
                final Account account = em.find(Account.class, accountId);
                if (null != account.getLastLoginDateTime()) {
                    final DateTime now = DateTime.now();
                    isLoggedIn =  Minutes.minutesBetween(new DateTime(account.getLastLoginDateTime()), now).getMinutes() < 20;
                    if (isLoggedIn) {
                        em.refresh(account, LockModeType.PESSIMISTIC_WRITE);
                        account.setLastLoginDateTime( new Timestamp(now.getMillis()));
                    }
                }
                return isLoggedIn;
            }
        });
    }

    public Account getAccountByEmail(final EntityManager em, final String email) {
        final TypedQuery<Account> query = em.createQuery("select a from Account a where a.email = :email", Account.class);
        query.setParameter("email", email);
        return asFluent(query.getResultList()).first().orNull();
    }

    public HashKey getHashKey(final EntityManager em, final String email, final String password) {
        return getHashKey(em, HashKey.generateHashCode(email, password));
    }
    public HashKey getHashKey(final EntityManager em, final Integer hashCode) {
        final TypedQuery<HashKey> hquery = em.createQuery("select a from HashKey a where a.hashCode = :hashCode", HashKey.class);
        hquery.setParameter("hashCode", hashCode);
        return asFluent(hquery.getResultList()).first().orNull();
    }

    public static Response buildResponse(Response.Status status) {
        return buildResponse(status, null);
    }
    public static Response buildResponse(Response.Status status, Object entity) {
        return Response
                .status(status)
                .entity(entity)
                .build();
    }
}