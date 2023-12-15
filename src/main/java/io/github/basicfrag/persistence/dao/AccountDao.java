package io.github.basicfrag.persistence.dao;

import io.github.basicfrag.persistence.model.Account;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ApplicationScoped
public class AccountDao implements IDao<Account> {

    @Inject
    EntityManager em;

    @Override
    @Transactional
    public void persistEntity(Account entity) {
        this.em.persist(entity);
    }

    @Override
    public Optional<List<Account>> findAllEntities() {

        TypedQuery<Account> query = this.em.createQuery("FROM Account", Account.class);
        List<Account> accountList = query.getResultList();

        if (accountList.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(query.getResultList());
    }

    @Override
    public Optional<Account> findEntityById(Long id) {
        System.out.println(id);
        Account account =  this.em.find(Account.class, id);
        return Optional.ofNullable(account);
    }

    @Override
    @Transactional
    public void updateEntity(Account data) {
        Account entity = this.em.merge(data);
    }

    @Override
    @Transactional
    public void removeEntity(Long id) {
        Account account = this.em.find(Account.class, id);
        this.em.remove(account);
    }
}
