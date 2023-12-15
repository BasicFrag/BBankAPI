package io.github.basicfrag.services;

import io.github.basicfrag.exceptions.BadRequestDataException;
import io.github.basicfrag.exceptions.ResourceNotFoundException;
import io.github.basicfrag.persistence.dao.AccountDao;
import io.github.basicfrag.persistence.dao.UserDao;
import io.github.basicfrag.persistence.dto.AccountDto;
import io.github.basicfrag.persistence.dto.UserDto;
import io.github.basicfrag.persistence.model.Account;
import io.github.basicfrag.persistence.model.User;
import io.github.basicfrag.validation.groups.AccountMandatoryInfo;
import io.github.basicfrag.validation.groups.TransactionInfo;
import io.github.basicfrag.validation.ValidationMessage;
import io.github.basicfrag.validation.groups.UserMandatoryInfo;
import io.github.basicfrag.validation.groups.UserUpdateInfo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.core.UriInfo;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class ServiceUtils {

    @Inject
    UserDao userDao;

    @Inject
    AccountDao accountDao;

    @Inject
    UriInfo uriInfo;

    @Inject
    Validator validator;


    Map<String, String> userNotFoundMessage() {
        Map<String, String> error = new LinkedHashMap<>();
        String uri = uriInfo.getRequestUri().toString();
        String usersResource = uriInfo.getBaseUri() + "/api/v1/users";

        error.put("error", "Resource not found!");
        error.put("reason", uri + " is an invalid/empty resource!");
        error.put("usersList", usersResource);
        error.put("createUser", usersResource);

        return error;
    }

    Map<String, String> internalServerErrorMessage() {
        Map<String, String> error = new LinkedHashMap<>();

        error.put("error", "Request processing wasn't successful!");
        error.put("reason", "An internal server error occurred! Please try again later!");

        return error;
    }

    public User dtoToUser(UserDto userDto) {
        User user = new User();

        user.setName(userDto.getName());
        user.setAge(userDto.getAge());
        user.setTelNumber(userDto.getTelNumber());
        user.setAddress(userDto.getAddress());

        return user;
    }

    public UserDto userToDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setName(user.getName());
        userDto.setAge(user.getAge());
        userDto.setTelNumber(user.getTelNumber());
        userDto.setAddress(user.getAddress());

        return userDto;
    }

    Boolean isUserUpdated(Long id, User data) {
        Optional<User> userOptional = this.userDao.findEntityById(id);
        User user = userOptional.
                orElseThrow(() -> new ResourceNotFoundException(userNotFoundMessage()));
        return data.equals(user);
    }

    Map<String, String> accountNotFoundMessage() {
        Map<String, String> message = new LinkedHashMap<>();
        String uri = uriInfo.getRequestUri().toString();
        String accountsResource = uriInfo.getBaseUri() + "api/v1/accounts";
        String usersResource = uriInfo.getBaseUri()+ "api/v1/users";

        message.put("error", "Resource not found!");
        message.put("reason", uri + " is an invalid/empty resource!");
        message.put("accountsList", accountsResource);
        message.put("usersList", usersResource);

        return message;
    }

    Boolean isAccountMandatoryInfoValidated(AccountDto data) {
        Set<ConstraintViolation<AccountDto>> violations = this.validator.validate(data, AccountMandatoryInfo.class);
        if (violations.isEmpty()) {
            return true;
        } else {
            Set<ValidationMessage> violationList = violations.stream()
                    .map((cv) -> {
                        ValidationMessage validationError = new ValidationMessage();
                        validationError.setMessage(cv.getMessage());
                        validationError.setInvalidValue(cv.getInvalidValue());
                        validationError.setPropertyName(cv.getPropertyPath().toString());
                        return validationError;
                    }).collect(Collectors.toSet());

            System.out.println(violations);
            throw new BadRequestDataException(violationList);
        }
    }
    Boolean isUserMandatoryInfoValidated(UserDto data) {
        Set<ConstraintViolation<UserDto>> violations = this.validator.validate(
                data, UserMandatoryInfo.class);
        if (violations.isEmpty()) {
            return true;
        } else {
            Set<ValidationMessage> violationSet = violations.stream()
                    .map((cv) -> {
                        ValidationMessage validationError = new ValidationMessage();
                        validationError.setMessage(cv.getMessage());
                        validationError.setInvalidValue(cv.getInvalidValue());
                        validationError.setPropertyName(cv.getPropertyPath().toString());
                        return validationError;
                    }).collect(Collectors.toSet());
            throw new BadRequestDataException(violationSet);
        }
    }
    void isUserUpdateInfoValidated(UserDto data) {
        Set<ConstraintViolation<UserDto>> violations = this.validator.validate(
                data,
                UserUpdateInfo.class
        );
         if(!violations.isEmpty()){
             Set<ValidationMessage> violationList = violations.stream()
                     .map((cv) -> {
                         ValidationMessage validationError = new ValidationMessage();
                         validationError.setMessage(cv.getMessage());
                         validationError.setInvalidValue(cv.getInvalidValue());
                         validationError.setPropertyName(cv.getPropertyPath().toString());
                         return validationError;
                     }).collect(Collectors.toSet());
            throw new BadRequestDataException(violationList);
        }
    }
    Boolean isTransactionInfoValidated(AccountDto data) {
        Set<ConstraintViolation<AccountDto>> violations = this.validator.validate(
                data,
                TransactionInfo.class
        );
        if (violations.isEmpty()) {
            return true;
        } else {
            Set<ValidationMessage> violationList = violations.stream()
                    .map((cv) -> {
                        ValidationMessage validationError = new ValidationMessage();
                        validationError.setMessage(cv.getMessage());
                        validationError.setInvalidValue(cv.getInvalidValue());
                        validationError.setPropertyName(cv.getPropertyPath().toString());
                        return validationError;
                    }).collect(Collectors.toSet());
            System.out.println(violationList);
            System.out.println(violations);
            throw new BadRequestDataException(violationList);
        }
    }

    Boolean isAccountUpdated(Long id, Account data) {
        Optional<Account> accountOptional = this.accountDao.findEntityById(id);
        Account account = accountOptional
                .orElseThrow(() -> new ResourceNotFoundException(accountNotFoundMessage()));
        return !data.equals(account);
    }

    public Account dtoToAccount(AccountDto accountDto, Long userId) {
        Optional<User> userOptional = this.userDao.findEntityById(userId);
        Account account = new Account();
        Map<String, String> error = accountNotFoundMessage();
        error.put("reason","Invalid or non-exiting user ID!");

        User user = userOptional
                .orElseThrow(() -> new ResourceNotFoundException(error));

        account.setAccountType(accountDto.getAccountType());
        account.setBalance(accountDto.getBalance());
        account.setUser(user);

        return account;
    }

    public AccountDto accountToDto(Account account) {

        AccountDto accountDto = new AccountDto();
        UserDto userDto = userToDto(account.getUser());

        accountDto.setAccountNumber(account.getAccountNumber());
        accountDto.setAccountType(account.getAccountType());
        accountDto.setBalance(account.getBalance());
        accountDto.setUserId(account.getUser().getId());
        accountDto.setUser(userDto);


        return accountDto;
    }
}
