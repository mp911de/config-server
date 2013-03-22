package de.paluch.configserver.rest;

import de.paluch.configserver.model.UnconfiguredHostException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 08.01.13 20:53
 */
public class UnconfiguredHostExceptionMapper implements ExceptionMapper<UnconfiguredHostException> {
    @Override
    public Response toResponse(UnconfiguredHostException e) {
        return Response.status(Response.Status.NOT_FOUND).
                type(MediaType.TEXT_PLAIN_TYPE).
                               entity("Cannot resolve " + e.getMessage() +
                                              " to an environment").build();
    }
}
