package io.github.basicfrag.persistence.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.basicfrag.persistence.model.AccountType;
import io.github.basicfrag.validation.groups.AccountMandatoryInfo;
import io.github.basicfrag.validation.groups.TransactionInfo;
import jakarta.enterprise.context.ApplicationScoped;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
@ApplicationScoped
public class AccountDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long accountNumber;

    @NotNull(message = "Field cannot be blank/null!", groups = AccountMandatoryInfo.class)
    AccountType accountType;

    @PositiveOrZero(message = "Field cannot be less than 0!",groups = TransactionInfo.class)
    @NotNull(message = "Field contains an invalid/null value! Only positive number values are valid!",groups = TransactionInfo.class)
    Double balance;

    @NotNull(message = "Field cannot be blank/null!",groups = AccountMandatoryInfo.class)
    @Digits(message = "Field needs an valid User ID!", integer = 2, fraction = 0,groups = AccountMandatoryInfo.class)
    @JsonProperty(value = "id",access = JsonProperty.Access.WRITE_ONLY)
    Long userId;

    UserDto user;


    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AccountDto{" +
                "accountNumber=" + accountNumber +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", userId=" + userId +
                ", user=" + user +
                '}';
    }
}
