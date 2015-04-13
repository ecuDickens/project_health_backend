package com.health.helper;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.health.base.ThrowingFunction1;
import com.health.entity.Account;
import com.health.exception.HttpException;
import com.health.jpa.session.EntitySession;
import com.health.jpa.spi.JpaEntityManagerService;
import com.health.types.ErrorType;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.sql.Timestamp;

@Singleton
public class JpaHelper {

    private JpaEntityManagerService jpaManagerService;

    private static String account_query = "SELECT i FROM Account i where i.id = :id";

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

    public Long createAccount(final Account account) throws HttpException {
        return executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
            @Override
            public Long apply(EntityManager em) throws HttpException {
                account
                        .withCreatedDatetime(new Timestamp(DateTime.now().getMillis()))
                        .withLastModifiedDatetime(new Timestamp(DateTime.now().getMillis()));
                em.persist(account);
                em.flush();
                return account.getId();
            }
        });
    }
    public Account getAccount(final Long accountId) throws HttpException {
        return executeJpa(new ThrowingFunction1<Account, EntityManager, HttpException>() {
            @Override
            public Account apply(EntityManager em) throws HttpException {
                return getAccount(accountId, em);
            }
        });
    }
    public Account getAccount(final Long accountId, final EntityManager em) {
        TypedQuery<Account> query = em.createQuery(account_query, Account.class);
        query.setParameter("id", accountId);
        return query.getSingleResult();
    }
    public Account updateAccount(final Account account) throws HttpException {
        return executeJpaTransaction(new ThrowingFunction1<Account, EntityManager, HttpException>() {
            @Override
            public Account apply(EntityManager em) throws HttpException {
                final Account accountForUpdate = getAccount(account.getId(), em);
                em.refresh(accountForUpdate, LockModeType.PESSIMISTIC_WRITE);
                accountForUpdate
                        .withEmail(Strings.isNullOrEmpty(account.getEmail()) ? accountForUpdate.getEmail() : account.getEmail())
                        .withFirstName(Strings.isNullOrEmpty(account.getFirstName()) ? accountForUpdate.getFirstName() : account.getFirstName())
                        .withLastName(Strings.isNullOrEmpty(account.getLastName()) ? accountForUpdate.getLastName() : account.getLastName())
                        .withMiddleInitial(Strings.isNullOrEmpty(account.getMiddleInitial()) ? accountForUpdate.getMiddleInitial() : account.getMiddleInitial())
                        .withGender(Strings.isNullOrEmpty(account.getGender()) ? accountForUpdate.getGender() : account.getGender())
                        .withLastModifiedDatetime(new Timestamp(DateTime.now().getMillis()));
                return account;
            }
        });
    }
}
