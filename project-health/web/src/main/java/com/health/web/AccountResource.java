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
import static com.health.helper.JpaHelper.buildResponse;
import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;
import static javax.ws.rs.core.Response.Status.*;

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
    private final RecipeResource recipeResource;
    private final FoodRecordResource foodRecordResource;
    private final ExerciseRecordResource exerciseRecordResource;
    private final SleepRecordResource sleepRecordResource;

    @Inject
    public AccountResource(JpaHelper jpaHelper,
                           EmailMatcher emailMatcher,
                           RecipeResource recipeResource,
                           FoodRecordResource foodRecordResource,
                           ExerciseRecordResource exerciseRecordResource,
                           SleepRecordResource sleepRecordResource) {
        this.jpaHelper = jpaHelper;
        this.emailMatcher = emailMatcher;
        this.recipeResource = recipeResource;
        this.foodRecordResource = foodRecordResource;
        this.exerciseRecordResource = exerciseRecordResource;
        this.sleepRecordResource = sleepRecordResource;
    }

    @POST
    public Response createAccount(@QueryParam("password") final String password, final Account account) throws HttpException {

        if (isNullOrEmpty(account.getFirstName()) || isNullOrEmpty(account.getLastName())) {
            return buildResponse(BAD_REQUEST, new ErrorType("First and Last Name required"));
        } else if (isNullOrEmpty(account.getEmail()) || !emailMatcher.validate(account.getEmail()) | isNullOrEmpty(password)) {
            return buildResponse(BAD_REQUEST, new ErrorType("Valid email address and password required"));
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
            return buildResponse(BAD_REQUEST, new ErrorType("Unable to create account"));
        }
        return buildResponse(OK, new Account().withId(accountId));
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
            return buildResponse(UNAUTHORIZED, new ErrorType("Login expired, please login again."));
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
            return buildResponse(NO_CONTENT, new ErrorType("Account not found"));
        }
        account.clean();
        return buildResponse(OK, account);
    }

    @POST
    @Path("/{account_id}")
    public Response updateAccount(@PathParam("account_id") final Long accountId,
                                  final Account account) throws HttpException {
        if (!jpaHelper.isLoggedIn(accountId)) {
            return buildResponse(UNAUTHORIZED, new ErrorType("Login expired, please login again."));
        }

        if (!isNullOrEmpty(account.getEmail())) {
            return buildResponse(BAD_REQUEST, new ErrorType("Unable to update email."));
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
        return buildResponse(OK);
    }

    /**---------------------------------- Recipe Calls ----------------------------------**/

    @POST
    @Path("/{account_id}/recipes")
    public Response createRecipe(@PathParam("account_id") final Long accountId, final Recipe recipe) throws HttpException {
        return recipeResource.createRecipe(accountId, recipe);
    }

    @GET
    @Path("/{account_id}/recipes")
    public Response loadRecipes(@PathParam("account_id") final Long accountId,
                                @QueryParam("name") final String name,
                                @QueryParam("id") final Long recipeId) throws HttpException {
        return recipeResource.loadRecipes(accountId, name, recipeId);
    }

    @POST
    @Path("/{account_id}/recipes/{recipe_id}")
    public Response updateRecipe(@PathParam("account_id") final Long accountId,
                                 @PathParam("recipe_id") final Long recipeId, final Recipe recipe) throws HttpException {
        return recipeResource.updateRecipe(accountId, recipeId, recipe);
    }

    @DELETE
    @Path("/{account_id}/recipes/{recipe_id}")
    public Response deleteRecipe(@PathParam("account_id") final Long accountId,
                                 @PathParam("recipe_id") final Long recipeId) throws HttpException {
        return recipeResource.deleteRecipe(accountId, recipeId);
    }

    /**---------------------------------- Food Record Calls ----------------------------------**/

    @POST
    @Path("/{account_id}/food_records")
    public Response createFoodRecord(@PathParam("account_id") final Long accountId, final FoodRecord record) throws HttpException {
        return foodRecordResource.createFoodRecord(accountId, record);
    }

    @GET
    @Path("/{account_id}/food_records")
    public Response loadFoodRecords(@PathParam("account_id") final Long accountId,
                                    @QueryParam("start_date") final Date startDate,
                                    @QueryParam("end_date") final Date endDate) throws HttpException {
        return foodRecordResource.loadFoodRecords(accountId, startDate, endDate);
    }

    @GET
    @Path("/{account_id}/food_records/{record_id}")
    public Response loadFoodRecord(@PathParam("account_id") final Long accountId,
                                   @PathParam("record_id") final Long recordId) throws HttpException {
        return foodRecordResource.loadFoodRecord(accountId, recordId);
    }

    @POST
    @Path("/{account_id}/food_records/{record_id}")
    public Response updateFoodRecord(@PathParam("account_id") final Long accountId,
                                     @PathParam("record_id") final Long recordId,
                                     final FoodRecord record) throws HttpException {
        return foodRecordResource.updateFoodRecord(accountId, recordId, record);
    }

    @DELETE
    @Path("/{account_id}/food_records/{record_id}")
    public Response deleteFoodRecord(@PathParam("account_id") final Long accountId,
                                     @PathParam("record_id") final Long recordId) throws HttpException {
        return foodRecordResource.deleteFoodRecord(accountId, recordId);
    }

    /**---------------------------------- Exercise Record Calls ----------------------------------**/

    @POST
    @Path("/{account_id}/exercise_records")
    public Response createExerciseRecord(@PathParam("account_id") final Long accountId,
                                         final ExerciseRecord record) throws HttpException {
        return exerciseRecordResource.createExerciseRecord(accountId, record);
    }

    @GET
    @Path("/{account_id}/exercise_records")
    public Response loadExerciseRecords(@PathParam("account_id") final Long accountId,
                                        @QueryParam("start_date") final Date startDate,
                                        @QueryParam("end_date") final Date endDate) throws HttpException {
        return exerciseRecordResource.loadExerciseRecords(accountId, startDate, endDate);
    }

    @GET
    @Path("/{account_id}/exercise_records/{record_id}")
    public Response loadExerciseRecord(@PathParam("account_id") final Long accountId,
                                       @PathParam("record_id") final Long recordId) throws HttpException {
        return exerciseRecordResource.loadExerciseRecord(accountId, recordId);
    }

    @POST
    @Path("/{account_id}/exercise_records/{record_id}")
    public Response updateExerciseRecord(@PathParam("account_id") final Long accountId,
                                         @PathParam("record_id") final Long recordId,
                                         final ExerciseRecord record) throws HttpException {
        return exerciseRecordResource.updateExerciseRecord(accountId, recordId, record);
    }

    @DELETE
    @Path("/{account_id}/exercise_records/{record_id}")
    public Response deleteExerciseRecord(@PathParam("account_id") final Long accountId,
                                         @PathParam("record_id") final Long recordId) throws HttpException {
        return exerciseRecordResource.deleteExerciseRecord(accountId, recordId);
    }

    /**---------------------------------- Sleep Record Calls ----------------------------------**/

    @POST
    @Path("/{account_id}/sleep_records")
    public Response createSleepRecord(@PathParam("account_id") final Long accountId,
                                      final SleepRecord record) throws HttpException {
        return sleepRecordResource.createSleepRecord(accountId, record);
    }

    @GET
    @Path("/{account_id}/sleep_records")
    public Response loadSleepRecords(@PathParam("account_id") final Long accountId,
                                     @QueryParam("start_date") final Date startDate,
                                     @QueryParam("end_date") final Date endDate) throws HttpException {
        return sleepRecordResource.loadSleepRecords(accountId, startDate, endDate);
    }

    @GET
    @Path("/{account_id}/sleep_records/{record_id}")
    public Response loadSleepRecord(@PathParam("account_id") final Long accountId,
                                    @PathParam("record_id") final Long recordId) throws HttpException {
        return sleepRecordResource.loadSleepRecord(accountId, recordId);
    }

    @POST
    @Path("/{account_id}/sleep_records/{record_id}")
    public Response updateSleepRecord(@PathParam("account_id") final Long accountId,
                                      @PathParam("record_id") final Long recordId,
                                      final SleepRecord record) throws HttpException {
        return sleepRecordResource.updateSleepRecord(accountId, recordId, record);
    }

    @DELETE
    @Path("/{account_id}/sleep_records/{record_id}")
    public Response deleteSleepRecord(@PathParam("account_id") final Long accountId,
                                      @PathParam("record_id") final Long recordId) throws HttpException {
        return sleepRecordResource.deleteSleepRecord(accountId, recordId);
    }
}