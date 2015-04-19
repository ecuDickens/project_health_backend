package com.health.web;

import com.google.inject.Inject;
import com.health.entity.SleepRecord;
import com.health.exception.HttpException;
import com.health.helper.JpaHelper;
import com.health.types.ErrorType;

import javax.ws.rs.core.Response;
import java.sql.Date;

public class SleepRecordResource {

    private final JpaHelper jpaHelper;

    @Inject
    public SleepRecordResource(JpaHelper jpaHelper) {
        this.jpaHelper = jpaHelper;
    }

    public Response createSleepRecord(final Long accountId, final SleepRecord record) throws HttpException {
        return Response.ok().entity(new ErrorType("Resource not implemented")).build();
    }

    public Response loadSleepRecords(final Long accountId, final Date startDate, final Date endDate) throws HttpException {
        return Response.ok().entity(new ErrorType("Resource not implemented")).build();
    }

    public Response loadSleepRecord(final Long accountId, final Long recordId) throws HttpException {
        return Response.ok().entity(new ErrorType("Resource not implemented")).build();
    }

    public Response updateSleepRecord(final Long accountId, final Long recordId, final SleepRecord record) throws HttpException {
        return Response.ok().entity(new ErrorType("Resource not implemented")).build();
    }

    public Response deleteSleepRecord(final Long accountId, final Long recordId) throws HttpException {
        return Response.ok().entity(new ErrorType("Resource not implemented")).build();
    }
}
