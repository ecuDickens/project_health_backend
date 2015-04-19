package com.health.web;

import com.google.inject.Inject;
import com.health.base.ThrowingFunction1;
import com.health.entity.Account;
import com.health.exception.HttpException;
import com.health.helper.JpaHelper;
import com.health.types.ErrorType;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Path("/logout")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class LogoutResource {

    private final JpaHelper jpaHelper;

    @Inject
    public LogoutResource(JpaHelper jpaHelper) {
        this.jpaHelper = jpaHelper;
    }

    @POST
    @Path("/{account_id}")
    public Response login(@PathParam("account_id") final Long accountId) throws HttpException {

        try {
            jpaHelper.executeJpaTransaction(new ThrowingFunction1<Integer, EntityManager, HttpException>() {
                @Override
                public Integer apply(EntityManager em) throws HttpException {
                    final Account forUpdate = em.find(Account.class, accountId);
                    if (null != forUpdate) {
                        em.refresh(forUpdate, PESSIMISTIC_WRITE);
                        forUpdate.setLastLoginDateTime(null);
                    }

                    return null;
                }
            });
        } catch (Exception e) {
            return Response
                    .status(BAD_REQUEST)
                    .entity(new ErrorType("Account not found."))
                    .build();
        }

        return Response.ok().build();
    }
}
