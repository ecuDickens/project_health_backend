package com.health.web;

import com.health.base.ThrowingFunction1;
import com.health.exception.HttpException;
import com.health.types.ErrorType;
import com.google.inject.Inject;
import com.health.entity.Profile;
import com.health.jpa.JpaHelper;
import org.joda.time.DateTime;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.sql.Timestamp;

@Path("/profiles")
public class ProfileResource {

    private final JpaHelper jpaHelper;

    @Inject
    public ProfileResource(JpaHelper jpaHelper) {
        this.jpaHelper = jpaHelper;
    }

    @POST
    public Response createProfile(@Context UriInfo uriInfo) throws HttpException {
        final MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        final String name = queryParams.get("name").get(0);
        final String email = queryParams.get("email").get(0);

        final Long profileId = jpaHelper.executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
            @Override
            public Long apply(EntityManager em) throws HttpException {
                final Profile profile = new Profile()
                        .withEmail(email)
                        .withName(name)
                        .withCreatedDatetime(new Timestamp(DateTime.now().getMillis()))
                        .withLastModifiedDatetime(new Timestamp(DateTime.now().getMillis()));
                em.persist(profile);
                em.flush();
                return profile.getId();
            }
        });

        if (null == profileId) {
            return Response.noContent()
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorType("Email and Name required"))
                    .build();
        }

        return Response.ok(new Profile().withId(profileId).toString()).build();
    }


    @GET
    @Path("/{profile_id}")
    public Response getProfile(@PathParam("profile_id") final Long profileId) throws HttpException {
        final Profile profile;
        try {
            profile = jpaHelper.executeJpa(new ThrowingFunction1<Profile, EntityManager, HttpException>() {
                @Override
                public Profile apply(EntityManager em) throws HttpException {

                    TypedQuery<Profile> query = em.createQuery("SELECT i FROM Profile i where i.id = :id", Profile.class);
                    query.setParameter("id", profileId);

                    return query.getSingleResult();
                }
            });
        } catch (NoResultException ex) {
            return Response.noContent()
                    .status(Response.Status.NOT_FOUND)
                    .entity("Profile not found")
                    .build();
        }
        return Response.ok(profile.toString()).build();
    }
}
