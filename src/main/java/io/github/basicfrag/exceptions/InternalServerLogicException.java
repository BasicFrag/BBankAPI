package io.github.basicfrag.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.Map;

public class InternalServerLogicException extends WebApplicationException {

    public InternalServerLogicException(Object errorMessage) {
        super(Response.status(500).entity(errorMessage).build());
    }
}
