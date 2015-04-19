package com.health.web;

import com.google.inject.Inject;
import com.health.base.ThrowingFunction1;
import com.health.entity.Account;
import com.health.entity.HashKey;
import com.health.exception.HttpException;
import com.health.helper.JpaHelper;
import com.health.types.ErrorType;
import org.joda.time.DateTime;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;

import static com.google.common.base.Strings.isNullOrEmpty;
import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Path("/login")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class LoginResource {

    private final JpaHelper jpaHelper;

    @Inject
    public LoginResource(JpaHelper jpaHelper) {
        this.jpaHelper = jpaHelper;
    }

    @POST
    public Response login(@QueryParam("email") final String email,
                          @QueryParam("password") final String password) throws HttpException {
        if (isNullOrEmpty(email) || isNullOrEmpty(password)) {
            return Response
                    .status(BAD_REQUEST)
                    .entity(new ErrorType("Email and password required."))
                    .build();
        }

        final Long accountId = jpaHelper.executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
            @Override
            public Long apply(EntityManager em) throws HttpException {
                final Account account = jpaHelper.getAccountByEmail(em, email);
                if (null != account) {
                    final HashKey hashKey = jpaHelper.getHashKey(em, email, password);
                    if (null != hashKey) {
                        em.refresh(account, LockModeType.PESSIMISTIC_WRITE);
                        account.setLastLoginDateTime( new Timestamp(DateTime.now().getMillis()));
                        return account.getId();
                    }
                }
                return null;
            }
        });

        if (null == accountId) {
            return Response
                    .status(BAD_REQUEST)
                    .entity(new ErrorType("Email or password was incorrect."))
                    .build();
        }

        return Response.ok(new Account().withId(accountId)).build();
    }

    @POST
    @Path("/{account_id}")
    public Response login(@PathParam("account_id") final Long accountId,
                          @QueryParam("email") final String email,
                          @QueryParam("password") final String password) throws HttpException {
        if (isNullOrEmpty(email) || isNullOrEmpty(password)) {
            return Response
                    .status(BAD_REQUEST)
                    .entity(new ErrorType("Email and password required."))
                    .build();
        }

        try {
            jpaHelper.executeJpaTransaction(new ThrowingFunction1<Integer, EntityManager, HttpException>() {
                @Override
                public Integer apply(EntityManager em) throws HttpException {
                    final Account forUpdate = em.find(Account.class, accountId);
                    final HashKey hashKey = jpaHelper.getHashKey(em, forUpdate.getHashKey());

                    em.refresh(hashKey, PESSIMISTIC_WRITE);
                    hashKey.setHashCode(HashKey.generateHashCode(email, password));

                    em.refresh(forUpdate, PESSIMISTIC_WRITE);
                    forUpdate.withEmail(email).withHashKey(hashKey.getHashCode());
                    return hashKey.getHashCode();
                }
            });
        } catch (Exception e) {
            return Response
                    .status(BAD_REQUEST)
                    .entity(new ErrorType("Email already in use."))
                    .build();
        }

        return Response.ok(new ErrorType("Email and password updated.")).build();
    }
}
