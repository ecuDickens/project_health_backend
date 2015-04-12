package com.health.jpa.session;

import com.health.exception.HttpException;
import javax.persistence.EntityManager;

/**
 * This interface defines the contact for a session that interacts with an
 * entity manager to perform one or more operations against a backing
 * persistence unit.
 */
public interface EntitySession<R> {

    /**
     * Executes one or more operations against the backing persistence unit
     * via the supplied entity manager reference.  The implementation should
     * return any object value necessary if needed, or simply return null if
     * there isn't a pertinent return value.  An invocation of a operation
     * that has void semantics should use the Void object type.
     *
     * @param entityManager
     *      A reference to the entity manager to interact with for
     *      the session.
     * @return
     *      A return object value or null.
     * @throws HttpException
     *      Thrown if there was a problem performing the execution.
     */
    public R execute(EntityManager entityManager)
            throws HttpException;

}