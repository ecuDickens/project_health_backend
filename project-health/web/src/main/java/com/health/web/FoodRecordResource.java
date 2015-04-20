package com.health.web;

import com.google.inject.Inject;
import com.health.entity.FoodRecord;
import com.health.exception.HttpException;
import com.health.helper.JpaHelper;
import com.health.types.ErrorType;

import javax.ws.rs.core.Response;
import java.sql.Date;

import static com.health.helper.JpaHelper.buildResponse;
import static javax.ws.rs.core.Response.Status.OK;

public class FoodRecordResource {

    private final JpaHelper jpaHelper;

    @Inject
    public FoodRecordResource(JpaHelper jpaHelper) {
        this.jpaHelper = jpaHelper;
    }

    public Response createFoodRecord(final Long accountId, final FoodRecord record) throws HttpException {
        return buildResponse(OK, new ErrorType("Resource not implemented"));
    }

    public Response loadFoodRecords(final Long accountId, final Date startDate, final Date endDate) throws HttpException {
        return buildResponse(OK, new ErrorType("Resource not implemented"));
    }

    public Response loadFoodRecord(final Long accountId, final Long recordId) throws HttpException {
        return buildResponse(OK, new ErrorType("Resource not implemented"));
    }

    public Response updateFoodRecord(final Long accountId, final Long recordId, final FoodRecord record) throws HttpException {
        return buildResponse(OK, new ErrorType("Resource not implemented"));
    }

    public Response deleteFoodRecord(final Long accountId, final Long recordId) throws HttpException {
        return buildResponse(OK, new ErrorType("Resource not implemented"));
    }
}
