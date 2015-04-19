package com.health.web;

import com.google.inject.Inject;
import com.health.base.ThrowingFunction1;
import com.health.collect.MoreCollections;
import com.health.entity.Exercise;
import com.health.exception.HttpException;
import com.health.helper.JpaHelper;
import com.health.types.ErrorType;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

@Path("/exercises")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class ExerciseResource {

    private final JpaHelper jpaHelper;

    @Inject
    public ExerciseResource(JpaHelper jpaHelper) {
        this.jpaHelper = jpaHelper;
    }

    @POST
    public Response createExercise(final Exercise exercise) throws HttpException {
        final Long exerciseId = jpaHelper.executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
            @Override
            public Long apply(EntityManager em) throws HttpException {
                em.persist(exercise);
                em.flush();
                return exercise.getId();
            }
        });

        if (null == exerciseId) {
            return Response
                    .status(BAD_REQUEST)
                    .entity(new ErrorType("Unable to create exercise."))
                    .build();
        }

        return Response.ok(new Exercise().withId(exerciseId)).build();
    }

    @GET
    public Response loadExercises(@QueryParam("name") final String name) throws HttpException {
        final List<Exercise> exercises = jpaHelper.executeJpa(new ThrowingFunction1<List<Exercise>, EntityManager, HttpException>() {
            @Override
            public List<Exercise> apply(EntityManager em) throws HttpException {
                final TypedQuery<Exercise> query;
                if (isNullOrEmpty(name)) {
                    query = em.createQuery("select a from Exercise a", Exercise.class);
                } else {
                    query = em.createQuery("select a from Exercise a where a.name LIKE :name", Exercise.class);
                    query.setParameter("name", name);
                }
                return query.getResultList();
            }
        });
        if (MoreCollections.isNullOrEmpty(exercises)) {
            return Response.noContent()
                    .entity(new ErrorType("Exercises not found"))
                    .build();
        }
        for (Exercise exercise : exercises) {
            exercise.clean();
        }
        return Response.ok(exercises).build();
    }

    @GET
    @Path("/{exercise_id}")
    public Response loadExercise(@PathParam("exercise_id") final Long exerciseId) throws HttpException {
        final Exercise exercise = jpaHelper.executeJpa(new ThrowingFunction1<Exercise, EntityManager, HttpException>() {
            @Override
            public Exercise apply(EntityManager em) throws HttpException {
                return em.find(Exercise.class, exerciseId);
            }
        });
        if (null == exercise) {
            return Response.noContent()
                    .entity(new ErrorType("Exercise not found"))
                    .build();
        }
        exercise.clean();
        return Response.ok(exercise).build();
    }



    @POST
    @Path("/{exercise_id}")
    public Response updateExercise(@PathParam("exercise_id") final Long exerciseId, final Exercise exercise) throws HttpException {
        jpaHelper.executeJpaTransaction(new ThrowingFunction1<Exercise, EntityManager, HttpException>() {
            @Override
            public Exercise apply(EntityManager em) throws HttpException {
                final Exercise forUpdate = em.find(Exercise.class, exerciseId);
                em.refresh(forUpdate, PESSIMISTIC_WRITE);
                forUpdate
                        .withName(isNullOrEmpty(exercise.getName()) ? forUpdate.getName() : exercise.getName())
                        .withDescription(null == exercise.getDescription() ? forUpdate.getDescription() : exercise.getDescription())
                        .withCaloriesBurnedPerHour(null == exercise.getCaloriesBurnedPerHour() ? forUpdate.getCaloriesBurnedPerHour() : exercise.getCaloriesBurnedPerHour());
                return forUpdate;
            }
        });
        return Response.ok().build();
    }

    @DELETE
    @Path("/{exercise_id}")
    public Response deleteExercise(@PathParam("exercise_id") final Long exerciseId) throws HttpException {
        jpaHelper.executeJpaTransaction(new ThrowingFunction1<Exercise, EntityManager, HttpException>() {
            @Override
            public Exercise apply(EntityManager em) throws HttpException {
                final Exercise exercise = em.find(Exercise.class, exerciseId);
                em.remove(exercise);
                return null;
            }
        });
        return Response.status(NO_CONTENT).build();
    }
}
