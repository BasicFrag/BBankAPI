package io.github.basicfrag.rest;

import io.github.basicfrag.exceptions.InternalServerLogicException;
import io.github.basicfrag.exceptions.ResourceNotFoundException;
import io.github.basicfrag.persistence.dto.UserDto;
import io.github.basicfrag.persistence.model.User;
import io.github.basicfrag.services.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.List;
import java.util.Map;

@Path("/api/v1/users")
public class UserController {

    @Inject
    UserService userService;

    @Operation(description = "Cadastra um novo usuário na API", summary = "Cria uma Usuário")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "CREATED; Conta criada e associada a um usuário com sucesso."),
            @APIResponse(responseCode = "400", description = "BAD REQUEST; Paramêtros inválidos no corpo (Body) da requisição"),
            @APIResponse(responseCode = "500", description = "INTERNAL SERVER ERROR; Erro interno na API")
    })
    @POST
    public Response createUser(@Valid UserDto userDto) {
        try {
            Map<String,String> message = this.userService.createUser(userDto);
            return Response.status(201).entity(message).build();
        } catch (WebApplicationException err) {
            return err.getResponse();
        }
    }

    @Operation(description = "Retorna um usuário cadastrado na API", summary = "Retorna um Usuário")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK; Usuário encontrado e retornado com sucesso."),
            @APIResponse(responseCode = "404", description = "NOT FOUND; Recurso 'Usuário' não existe ou o ID fornecido é inválido"),
            @APIResponse(responseCode = "500", description = "INTERNAL SERVER ERROR; Erro interno na API")
    })
    @GET
    @Path("/{id}")
    public Response getResourceById(@PathParam("id") Long id) {
        try {
        UserDto user = this.userService.getUserById(id);
        return Response.status(200).entity(user).build();
        } catch (ResourceNotFoundException err) {
            return err.getResponse();
        }
    }
    @Operation(description = "Retorna todos os usuários", summary = "Retorna uma lista de usuários")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK; Usuários encontrados com sucesso."),
            @APIResponse(responseCode = "404", description = "NOT FOUND; Recurso 'Usuário' não existe na API"),
            @APIResponse(responseCode = "500", description = "INTERNAL SERVER ERROR; Erro interno na API")
    })
    @GET
    public Response getAll() {
        try {
        List<User> userList = this.userService.getAll();
        return Response.status(200).entity(userList).build();
        } catch (ResourceNotFoundException err) {
            return err.getResponse();
        }
    }

    @Operation(description = "Atualiza um usuário com algumas informações", summary = "Atualiza um usuário")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK; Usuário atualizado com sucesso."),
            @APIResponse(responseCode = "404", description = "NOT FOUND; Recurso 'Usuário' não existe na API"),
            @APIResponse(responseCode = "400", description = "BAD REQUEST; Paramêtros no corpo (Body) da requisição inválidos"),
            @APIResponse(responseCode = "500", description = "INTERNAL SERVER ERROR; Erro interno na API")
    })
    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id,@Valid UserDto data) {
        try {
            Map<String, String> message = this.userService.updateUser(id, data);
            return Response.status(200).entity(message).build();
        } catch (ResourceNotFoundException err) {
            return err.getResponse();
        }
    }

    @Operation(description = "Remove um usuário, caso esse ainda não tenha uma conta associada", summary = "Remove um usuário")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK; Usuário removido com sucesso."),
            @APIResponse(responseCode = "404", description = "NOT FOUND; Recurso 'Usuário' não existe na API"),
            @APIResponse(responseCode = "500", description = "INTERNAL SERVER ERROR; Erro interno na API")
    })
    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id")Long id) {
        try {
            Map<String, String> removedUserMessage = this.userService.removeUser(id);
            return Response.status(200).entity(removedUserMessage).build();
        } catch (InternalServerLogicException err) {
            return err.getResponse();
        }
    }

}
