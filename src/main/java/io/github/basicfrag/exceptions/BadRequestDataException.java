package io.github.basicfrag.exceptions;

import io.github.basicfrag.persistence.dto.AccountDto;
import io.github.basicfrag.persistence.dto.UserDto;
import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.Set;

public class BadRequestDataException extends WebApplicationException {


    public BadRequestDataException(Object errorMessage) {
        super(Response.status(400).entity(errorMessage).build());
    }

}
