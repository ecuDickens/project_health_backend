package com.health.jpa;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.health.base.ThrowingFunction1;
import com.health.exception.HttpException;
import com.health.jpa.session.EntitySession;
import com.health.jpa.spi.JpaEntityManagerService;

import javax.persistence.EntityManager;

@Singleton
public class JpaHelper {

    private JpaEntityManagerService jpaManagerService;

    @Inject
    public JpaHelper(JpaEntityManagerService jpaManagerService) {
        this.jpaManagerService = jpaManagerService;
    }

    public <T> T executeJpa(final ThrowingFunction1<T, EntityManager, HttpException> jpaFunction) throws HttpException {
        return jpaManagerService.invoke(new EntitySession<T>() {
            @Override
            public T execute(EntityManager entityManager) throws HttpException {
                return jpaFunction.apply(entityManager);
            }
        });
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
                } catch (Throwable t) {
                    rollback = true;
                } finally {
                    if (rollback && entityManager.getTransaction().isActive()) {
                        entityManager.getTransaction().rollback();
                    }
                }
                return result;
            }
        });
    }
}
