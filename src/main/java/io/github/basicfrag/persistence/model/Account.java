package io.github.basicfrag.persistence.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long accountNumber;
    @Enumerated(EnumType.STRING)
    AccountType accountType;

    Double balance;

    @OneToOne
    @JoinColumn( name = "user_id")
    User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber) && accountType == account.accountType && Objects.equals(balance, account.balance) && Objects.equals(user, account.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, accountType, balance, user);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", user=" + user +
                '}';
    }

}
