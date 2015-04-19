package com.health.web;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.health.base.ThrowingFunction1;
import com.health.collect.MoreCollections;
import com.health.datetime.DateTimeUtils;
import com.health.entity.*;
import com.health.exception.HttpException;
import com.health.helper.JpaHelper;
import com.health.matchers.EmailMatcher;
import com.health.types.ErrorType;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.health.helper.JpaHelper.LOGGED_OUT_RESPONSE;
import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * Contains calls for creating, loading, and updating account and related child records.
 * Child calls are passed to the related child resource for processing.
 */
@Path("/accounts")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class AccountResource {

    private final JpaHelper jpaHelper;
    private final EmailMatcher emailMatcher;

    @Inject
    public AccountResource(JpaHelper jpaHelper, EmailMatcher emailMatcher) {
        this.jpaHelper = jpaHelper;
        this.emailMatcher = emailMatcher;
    }

    @POST
    public Response createAccount(@QueryParam("password") final String password, final Account account) throws HttpException {
        String error = "";
        if (isNullOrEmpty(account.getFirstName()) || isNullOrEmpty(account.getLastName())) {
            error = "First and Last Name required";
        } else if (isNullOrEmpty(account.getEmail()) || !emailMatcher.validate(account.getEmail()) | isNullOrEmpty(password)) {
            error = "Valid email address and password required";
        }

        if (!isNullOrEmpty(error)) {
            return Response
                    .status(BAD_REQUEST)
                    .entity(new ErrorType(error))
                    .build();
        }

        final Long accountId = jpaHelper.executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
            @Override
            public Long apply(EntityManager em) throws HttpException {
                final HashKey hashKey = new HashKey().withHashCode(HashKey.generateHashCode(account.getEmail(), password));
                em.persist(hashKey);
                em.flush();

                account.setHashKey(hashKey.getHashCode());
                em.persist(account);
                em.flush();
                return account.getId();
            }
        });

        if (null == accountId) {
            return Response
                    .status(BAD_REQUEST)
                    .entity(new ErrorType("Unable to create account"))
                    .build();
        }

        return Response.ok(new Account().withId(accountId)).build();
    }


    @GET
    @Path("/{account_id}")
    public Response getAccount(@PathParam("account_id") final Long accountId,
                               @QueryParam("return_sleep") final Boolean returnSleep,
                               @QueryParam("return_exercise") final Boolean returnExercise,
                               @QueryParam("return_food") final Boolean returnFood,
                               @QueryParam("start_date") final String startDate,
                               @QueryParam("end_date") final String endDate) throws HttpException {
        if (!jpaHelper.isLoggedIn(accountId)) {
            return LOGGED_OUT_RESPONSE;
        }

        final Date parsedStartDate = !isNullOrEmpty(startDate) ? new Date(DateTimeUtils.parse(startDate).getMillis()) : null;
        final Date parsedEndDate = !isNullOrEmpty(endDate) ? new Date(DateTimeUtils.parse(endDate).getMillis()) : null;
        final Account account = jpaHelper.executeJpa(new ThrowingFunction1<Account, EntityManager, HttpException>() {
            @Override
            public Account apply(EntityManager em) throws HttpException {
                final Account account = em.find(Account.class, accountId);
                if (null == returnExercise || !returnExercise) {
                    account.setExerciseRecords(null);
                } else if ((null != parsedStartDate || null != parsedEndDate) && !MoreCollections.isNullOrEmpty(account.getExerciseRecords())) {
                    final List<ExerciseRecord> exerciseRecords = Lists.newArrayList();
                    for (ExerciseRecord record : account.getExerciseRecords()) {
                        if ((null == parsedStartDate || record.getRecordDate().compareTo(parsedStartDate) >= 0) &&
                            (null == parsedEndDate || record.getRecordDate().compareTo(parsedEndDate) <= 0)) {
                            exerciseRecords.add(record);
                        }
                    }
                    account.setExerciseRecords(exerciseRecords);
                }
                if (null == returnSleep || !returnSleep) {
                    account.setSleepRecords(null);
                } else if ((null != parsedStartDate || null != parsedEndDate) && !MoreCollections.isNullOrEmpty(account.getSleepRecords())) {
                    final List<SleepRecord> sleepRecords = Lists.newArrayList();
                    for (SleepRecord record : account.getSleepRecords()) {
                        if ((null == parsedStartDate || record.getRecordDate().compareTo(parsedStartDate) >= 0) &&
                                (null == parsedEndDate || record.getRecordDate().compareTo(parsedEndDate) <= 0)) {
                            sleepRecords.add(record);
                        }
                    }
                    account.setSleepRecords(sleepRecords);
                }
                if (null == returnFood || !returnFood) {
                    account.setFoodRecords(null);
                } else if ((null != parsedStartDate || null != parsedEndDate) && !MoreCollections.isNullOrEmpty(account.getFoodRecords())) {
                    final List<FoodRecord> foodRecords = Lists.newArrayList();
                    for (FoodRecord record : account.getFoodRecords()) {
                        if ((null == parsedStartDate || record.getRecordDate().compareTo(parsedStartDate) >= 0) &&
                                (null == parsedEndDate || record.getRecordDate().compareTo(parsedEndDate) <= 0)) {
                            foodRecords.add(record);
                        }
                    }
                    account.setFoodRecords(foodRecords);
                }
                return account;
            }
        });
        if (null == account) {
            return Response.noContent()
                    .entity(new ErrorType("Account not found"))
                    .build();
        }
        account.clean();
        return Response.ok(account).build();
    }

    @POST
    @Path("/{account_id}")
    public Response updateAccount(@PathParam("account_id") final Long accountId,
                                  final Account account) throws HttpException {
        if (!jpaHelper.isLoggedIn(accountId)) {
            return LOGGED_OUT_RESPONSE;
        }

        if (!isNullOrEmpty(account.getEmail())) {
            return Response
                    .status(BAD_REQUEST)
                    .entity(new ErrorType("Unable to update email."))
                    .build();
        }

        jpaHelper.executeJpaTransaction(new ThrowingFunction1<Account, EntityManager, HttpException>() {
            @Override
            public Account apply(EntityManager em) throws HttpException {
                final Account forUpdate = em.find(Account.class, accountId);
                em.refresh(forUpdate, PESSIMISTIC_WRITE);
                forUpdate
                        .withFirstName(isNullOrEmpty(account.getFirstName()) ? forUpdate.getFirstName() : account.getFirstName())
                        .withLastName(isNullOrEmpty(account.getLastName()) ? forUpdate.getLastName() : account.getLastName())
                        .withMiddleName(isNullOrEmpty(account.getMiddleName()) ? forUpdate.getMiddleName() : account.getMiddleName())
                        .withGender(isNullOrEmpty(account.getGender()) ? forUpdate.getGender() : account.getGender())
                        .withIsShareAccount(null == account.getIsShareAccount() ? forUpdate.getIsShareAccount() : account.getIsShareAccount())
                        .withLastName(isNullOrEmpty(account.getLastName()) ? forUpdate.getLastName() : account.getLastName());
                return forUpdate;
            }
        });
        return Response.ok().build();
    }
}
