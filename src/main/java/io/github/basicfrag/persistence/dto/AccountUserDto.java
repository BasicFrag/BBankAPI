package io.github.basicfrag.persistence.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.basicfrag.persistence.model.AccountType;

public class AccountUserDto extends AccountDto{
    private Long accountNumber;

    private AccountType accountType;

    private Double balance;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserDto user;

    public AccountUserDto() {
        super();
    }
    @Override
    public Long getAccountNumber() {
        return accountNumber;
    }

    @Override
    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public AccountType getAccountType() {
        return accountType;
    }

    @Override
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    @Override
    public Double getBalance() {
        return balance;
    }

    @Override
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public UserDto getUser() {
        return user;
    }

    @Override
    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getUsername() {
        return user.getName();
    }

}
