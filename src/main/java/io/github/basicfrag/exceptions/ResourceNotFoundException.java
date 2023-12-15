package io.github.basicfrag.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.Map;

public class ResourceNotFoundException  extends WebApplicationException {
    public ResourceNotFoundException(Object message) {
        super(Response.status(404).entity(message).build());
    }

}
