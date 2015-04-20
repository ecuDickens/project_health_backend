package com.health.web;

import com.google.inject.Inject;
import com.health.entity.ExerciseRecord;
import com.health.exception.HttpException;
import com.health.helper.JpaHelper;
import com.health.types.ErrorType;

import javax.ws.rs.core.Response;
import java.sql.Date;

import static com.health.helper.JpaHelper.buildResponse;
import static javax.ws.rs.core.Response.Status.OK;

public class ExerciseRecordResource {

    private final JpaHelper jpaHelper;

    @Inject
    public ExerciseRecordResource(JpaHelper jpaHelper) {
        this.jpaHelper = jpaHelper;
    }

    public Response createExerciseRecord(final Long accountId, final ExerciseRecord record) throws HttpException {
        return buildResponse(OK, new ErrorType("Resource not implemented"));
    }

    public Response loadExerciseRecords(final Long accountId, final Date startDate, final Date endDate) throws HttpException {
        return buildResponse(OK, new ErrorType("Resource not implemented"));
    }

    public Response loadExerciseRecord(final Long accountId, final Long recordId) throws HttpException {
        return buildResponse(OK, new ErrorType("Resource not implemented"));
    }

    public Response updateExerciseRecord(final Long accountId, final Long recordId, final ExerciseRecord record) throws HttpException {
        return buildResponse(OK, new ErrorType("Resource not implemented"));
    }

    public Response deleteExerciseRecord(final Long accountId, final Long recordId) throws HttpException {
        return buildResponse(OK, new ErrorType("Resource not implemented"));
    }
}
