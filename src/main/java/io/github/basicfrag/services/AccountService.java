package io.github.basicfrag.services;

import io.github.basicfrag.exceptions.BadRequestDataException;
import io.github.basicfrag.exceptions.InternalServerLogicException;
import io.github.basicfrag.exceptions.ResourceNotFoundException;
import io.github.basicfrag.persistence.dao.AccountDao;
import io.github.basicfrag.persistence.dto.AccountDto;
import io.github.basicfrag.persistence.dto.AccountUserDto;
import io.github.basicfrag.persistence.dto.UserDto;
import io.github.basicfrag.persistence.model.Account;
import io.github.basicfrag.persistence.model.AccountType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.UriInfo;

import java.util.*;

@ApplicationScoped
public class AccountService {

    @Inject
    AccountDao accountDao;

    @Inject
    UriInfo uriInfo;

    @Inject
    ServiceUtils utils;


    public AccountDto getById(Long id) {
        Optional<Account> accountOptional = this.accountDao.findEntityById(id);
        Map<String, String> error = this.utils.accountNotFoundMessage();

        Account account = accountOptional
                .orElseThrow(() -> new ResourceNotFoundException(error));

        return this.utils.accountToDto(account);
    }

    public List<AccountUserDto> getAll() {
        Optional<List<Account>> optionalAccountList = this.accountDao.findAllEntities();
        List<Account> accountList;
        Map<String, String> error = this.utils.accountNotFoundMessage();
        List<AccountUserDto> accountDtoList = new ArrayList<>();

        accountList = optionalAccountList
                .orElseThrow(() -> new ResourceNotFoundException(error));
        for (Account account : accountList) {
            AccountUserDto accountUserDto = new AccountUserDto();

            UserDto userDto = this.utils.userToDto(account.getUser());
            accountUserDto.setAccountNumber(account.getAccountNumber());
            accountUserDto.setAccountType(account.getAccountType());
            accountUserDto.setBalance(account.getBalance());
            accountUserDto.setUser(userDto);

            accountDtoList.add(accountUserDto);
        }
        return accountDtoList;

    }

    public Map<String, String> createAccount(AccountDto data) {
        if (this.utils.isAccountMandatoryInfoValidated(data)) {
            Account account = this.utils.dtoToAccount(data, data.getUserId());
            account.setBalance(0.0);
            this.accountDao.persistEntity(account);
        }

        Map<String, String> message = new LinkedHashMap<>();
        String accountsResource = uriInfo.getRequestUri().toString();
        String deleteAccountResource = uriInfo.getRequestUri() + "{resourceId}";
        String updateAccountResource = uriInfo.getRequestUri() + "{resourceId}";

        message.put("message", "Sucess upon persisting Account");
        message.put("GET", accountsResource);
        message.put("PUT", updateAccountResource);
        message.put("PATCH", updateAccountResource);
        message.put("DELETE", deleteAccountResource);
        return message;
    }

    public Map<String, String> updateAccount(Long id, AccountDto accountDto) {
        this.utils.isTransactionInfoValidated(accountDto);

        Optional<Account> accountOptional = this.accountDao.findEntityById(id);
        Map<String, String> error = this.utils.accountNotFoundMessage();
        Account updatedAccountState = accountOptional
                .orElseThrow(() -> new ResourceNotFoundException(error));

        updatedAccountState.setBalance(accountDto.getBalance());
        this.accountDao.updateEntity(updatedAccountState);

        Map<String, String> message = new LinkedHashMap<>();
        String accountResource = uriInfo.getBaseUri() + "api/v1/accounts";
        String createAccountResource = uriInfo.getBaseUri() + "api/v1/accounts";
        String deleteAccountResource = uriInfo.getBaseUri() + "api/v1/accounts";

        if (this.utils.isAccountUpdated(id, updatedAccountState)) {
            message.put("message", "Success upon updating resource!");
            message.put("accountsList", accountResource);
            message.put("createAccount", createAccountResource);
            message.put("deleteAccount", deleteAccountResource);
        } else {
            throw new InternalServerLogicException(this.utils.internalServerErrorMessage());
        }
        return message;
    }

    public Map<String, String> removeAccount(Long id) throws InternalServerLogicException {
        Map<String, String> message = this.utils.internalServerErrorMessage();
        String accountResource = uriInfo.getBaseUri() + "accounts";
        Optional<Account> removedAccount = this.accountDao.findEntityById(id);
        removedAccount.orElseThrow(() -> new ResourceNotFoundException(this.utils.accountNotFoundMessage()));
        this.accountDao.removeEntity(id);


            message.clear();
            message.put("message", "Success upon deleting resource!");
            message.put("accountsList", accountResource);
            message.put("createAccount", accountResource);


        return message;
    }

    public Map<String, String> withdraw(Long id,@Valid AccountDto balance) {
        Optional<Account> accountOptional = this.accountDao.findEntityById(id);
        Map<String, String> message = new LinkedHashMap<>();
        String accountsResource = uriInfo.getBaseUri() + "api/v1/accounts";

        Account account = accountOptional
                .orElseThrow(() -> new ResourceNotFoundException(this.utils.accountNotFoundMessage()));

        Double withdrawAmount = balance.getBalance();
        Boolean transactionInfoValidated = this.utils.isTransactionInfoValidated(balance);
        if (transactionInfoValidated) {
            withdrawAmount = account.getBalance() - withdrawAmount;
            if (withdrawAmount < 0) {
                message.put("error", "Invalid Request!");
                message.put("reason", "Insufficient funds to complete transaction!");
                throw new BadRequestDataException(message);
            } else {
                account.setBalance(withdrawAmount);
                this.accountDao.updateEntity(account);

                message.put("message", "Transaction completed!");
                message.put("accountsList", accountsResource);
            }
        }

        return message;
    }

    public Map<String, String> deposit(Long id, @Valid AccountDto balance) {
        Optional<Account> accountOptional = this.accountDao.findEntityById(id);
        Map<String, String> message = new LinkedHashMap<>();
        String accountsResource = uriInfo.getBaseUri() + uriInfo.getPath() + "accounts";
        Account account = accountOptional
                .orElseThrow(() -> new ResourceNotFoundException(this.utils.accountNotFoundMessage()));
        Double depositAmount = balance.getBalance();

        if (this.utils.isTransactionInfoValidated(balance)) {
            if (account.getAccountType() == AccountType.CONTA_POUPANCA) {
                depositAmount += 0.5;
            }
            depositAmount = account.getBalance() + depositAmount;
            account.setBalance(depositAmount);
            this.accountDao.updateEntity(account);

            message.put("message", "Transaction completed!");
            message.put("accountsList", accountsResource);
            return message;
        } else {
            message = this.utils.internalServerErrorMessage();
            return message;
        }
    }

}
