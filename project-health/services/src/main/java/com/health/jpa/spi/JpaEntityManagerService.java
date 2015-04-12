package com.health.jpa.spi;

import com.health.exception.HttpException;
import com.health.jpa.session.EntitySession;
import com.health.spi.ExternalService;

import javax.persistence.EntityManager;

/**
 * This service contract defines operations for interacting with Java Persistence API (JPA)
 * EntityManager object.  The operations exposed on this service interface provide course
 * grained access to the underlying EntityManager object, and attempt to keep out of the
 * way of hiding the publicly documented EntityManager API.
 */
public interface JpaEntityManagerService extends ExternalService {

    /**
     * Invokes one or more actions against the backing persistence unit as expressed in
     * the supplied EntitySession implementation.  A result object value can be returned
     * if necessary to the supplied EntitySession implementation or simply null if a
     * return value is not necessary.  A default number of attempts are made to invoke
     * the supplied session in the case that a given invocation fails.
     *
     * @param session
     *      An EntitySession implementation object which defines the actions to take
     *      during the invocation.
     * @return
     *      A return object value or null.
     * @throws HttpException
     *      Thrown if there was a problem during the invocation of the supplied session.
     */
    public <R> R invoke(EntitySession<R> session)
            throws HttpException;

    /**
     * Invokes one or more actions against the backing persistence unit as expressed in
     * the supplied EntitySession implementation.  A result object value can be returned
     * if necessary to the supplied EntitySession implementation or simply null if a
     * return value is not necessary.  The specified number of attempts will be made in
     * the case that a given invocation fails.
     *
     * @param attempts
     *      A positive non-zero integer value representing the number of times to attempt
     *      to invoke the supplied session in the case a given invocation fails.
     * @param session
     *      An EntitySession implementation object which defines the actions to take
     *      during the invocation.
     * @return
     *      A return object value or null.
     * @throws HttpException
     *      Thrown if there was a problem during the invocation of the supplied session.
     */
    public <R> R invoke(int attempts, EntitySession<R> session)
            throws HttpException;

    /**
     * Creates and returns a new, unmanaged EntityManager. The lifecycle for the returned
     * EntityManager is unmanaged and it must be closed explicitly by the calling application.
     *
     * @return
     *      A new EntityManager
     */
    public EntityManager createEntityManager();

}