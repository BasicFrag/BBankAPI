package io.github.basicfrag.rest;

import io.github.basicfrag.exceptions.BadRequestDataException;
import io.github.basicfrag.persistence.dto.AccountDto;
import io.github.basicfrag.persistence.dto.AccountUserDto;
import io.github.basicfrag.services.AccountService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("/api/v1/accounts")
public class AccountController {

    @Inject
    AccountService accountService;


    @Operation(description = "Cria uma conta e associa essa conta a um usuário", summary = "Cria uma conta")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "CREATED; Conta criada e associada a um usuário com sucesso."),
            @APIResponse(responseCode = "400", description = "BAD REQUEST; Paramêtros inválidos no corpo (Body) da requisição"),
            @APIResponse(responseCode = "404", description = "NOT FOUND; ID do Usuário fornecido no corpo (Body) da requisição é inválido ou não existe."),
            @APIResponse(responseCode = "500", description = "INTERNAL SERVER ERROR; Erro interno na API")
    })
    @POST
    public Response createAccount(@Valid AccountDto accountDto) {
        try {
            Map<String, String> message = this.accountService.createAccount(accountDto);
            return Response.status(201).entity(message).build();
        } catch (BadRequestDataException err) {
            return err.getResponse();
        }

    }

    @Operation(description = "Retorna uma Conta pelo seu número (ID)", summary = "Retorna uma Conta")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK; Conta associada a um usuário encontrada e retornada com sucesso."),
            @APIResponse(responseCode = "404", description = "NOT FOUND; Recurso 'Conta' não existe ou o ID fornecido é inválido"),
            @APIResponse(responseCode = "500", description = "INTERNAL SERVER ERROR; Erro interno na API")
    })
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        AccountDto accountDto = this.accountService.getById(id);
        return Response.status(200).entity(accountDto).build();
    }

    @Operation(description = "Retorna todas as Contas e suas informações cadastradas", summary = "Retorna uma lista de Contas")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK; Contas encontradas com sucesso."),
            @APIResponse(responseCode = "404", description = "NOT FOUND; Recurso 'Conta' não existe na API"),
            @APIResponse(responseCode = "500", description = "INTERNAL SERVER ERROR; Erro interno na API")
    })
    @GET
    public Response getAll() {
        try {
            List<AccountUserDto> accountList = this.accountService.getAll();
            return Response.status(200).entity(accountList).build();
        } catch (WebApplicationException err) {
            return err.getResponse();
        }
    }

    @Operation(description = "Atualiza uma conta com algumas informações", summary = "Atualiza uma conta")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK; Conta associada a um usuário encontrada e atualizada com sucesso."),
            @APIResponse(responseCode = "404", description = "NOT FOUND; Recurso 'Conta' não existe ou o ID fornecido é inválido"),
            @APIResponse(responseCode = "500", description = "INTERNAL SERVER ERROR; Erro interno na API")
    })
    @PUT
    @Path("/{id}")
    public Response updateAccount(@PathParam("id") Long id, @Valid AccountDto accountDto) {
        try {
            Map<String, String> message = this.accountService.updateAccount(id, accountDto);
            return Response.status(200).entity(message).build();
        } catch (WebApplicationException err) {
            return err.getResponse();
        }
    }

    @Operation(description = "Remove uma conta e o usuário associado a ela", summary = "Remove uma conta")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK; Conta encontrada e removida (incluindo o usuário associado essa conta) com sucesso."),
            @APIResponse(responseCode = "404", description = "NOT FOUND; Recurso 'Conta' não existe ou o ID fornecido é inválido"),
            @APIResponse(responseCode = "500", description = "INTERNAL SERVER ERROR; Erro interno na API")
    })
    @DELETE
    @Path("/{id}")
    public Response deleteAccount(@PathParam("id") Long id) {
        try {
            Map<String, String> message = this.accountService.removeAccount(id);
            return Response.status(200).entity(message).build();
        } catch (WebApplicationException err) {
            return err.getResponse();
        }
    }

    @Operation(description = "Realiza uma transação (saque ou depósito) de um valor em uma conta cadastrada na API", summary = "Transação de um valor da conta")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK; Saque relizado com sucesso"),
            @APIResponse(responseCode = "404", description = "NOT FOUND; Recurso 'Conta' não existe ou ID fornecido (Path Parameter) é inválido"),
            @APIResponse(responseCode = "400", description = "BAD REQUEST; Paramêtro 'balance' no corpo (Body) da requisição inválido ou conta tem o saldo insuficiente para realizar a operação."),
            @APIResponse(responseCode = "400", description = "BAD REQUEST; Paramêtro 'operation' na URL (Query Parameter) da requisição inválido ou nulo"),
            @APIResponse(responseCode = "500", description = "INTERNAL SERVER ERROR; Erro interno na API")
    })
    @PATCH
    @Path("/{id}")
    public Response transaction(
            @PathParam("id") Long id,
            @QueryParam("operation") String operation,
             AccountDto amount) {
        Map<String, String> message = new LinkedHashMap<>();
        try {
            if (operation.equals("deposit")) {
                message = this.accountService.deposit(id, amount);
                return Response.status(200).entity(message).build();
            }
            if (operation.equals("withdraw")) {
                message = this.accountService.withdraw(id, amount);
                return Response.status(200).entity(message).build();
            }

            message.put("error", "Invalid Request!");
            message.put("reason", "Invalid query parameters!");
            message.put("validQueryParam1", "operation = 'withdraw'");
            message.put("validQueryParam2", "operation = 'deposit'");

           throw new BadRequestDataException(message);
        } catch (WebApplicationException err) {
            if (err.getResponse().getStatus() == 500) {
                message.clear();
                message.put("error", "Invalid Request!");
                message.put("reason", "'operation' Query Parameter is null!");
                return Response.status(400).entity(message).build();
            }
            return err.getResponse();
        }
    }


}
