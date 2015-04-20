package com.health.web;

import com.google.inject.Inject;
import com.health.entity.SleepRecord;
import com.health.exception.HttpException;
import com.health.helper.JpaHelper;
import com.health.types.ErrorType;

import javax.ws.rs.core.Response;
import java.sql.Date;

import static com.health.helper.JpaHelper.buildResponse;
import static javax.ws.rs.core.Response.Status.OK;

public class SleepRecordResource {

    private final JpaHelper jpaHelper;

    @Inject
    public SleepRecordResource(JpaHelper jpaHelper) {
        this.jpaHelper = jpaHelper;
    }

    public Response createSleepRecord(final Long accountId, final SleepRecord record) throws HttpException {
        return buildResponse(OK, new ErrorType("Resource not implemented"));
    }

    public Response loadSleepRecords(final Long accountId, final Date startDate, final Date endDate) throws HttpException {
        return buildResponse(OK, new ErrorType("Resource not implemented"));
    }

    public Response loadSleepRecord(final Long accountId, final Long recordId) throws HttpException {
        return buildResponse(OK, new ErrorType("Resource not implemented"));
    }

    public Response updateSleepRecord(final Long accountId, final Long recordId, final SleepRecord record) throws HttpException {
        return buildResponse(OK, new ErrorType("Resource not implemented"));
    }

    public Response deleteSleepRecord(final Long accountId, final Long recordId) throws HttpException {
        return buildResponse(OK, new ErrorType("Resource not implemented"));
    }
}
