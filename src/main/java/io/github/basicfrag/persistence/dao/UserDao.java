package io.github.basicfrag.persistence.dao;

import io.github.basicfrag.exceptions.InternalServerLogicException;
import io.github.basicfrag.persistence.model.Account;
import io.github.basicfrag.persistence.model.User;
import io.github.basicfrag.validation.ValidationMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class UserDao implements IDao<User> {

    @Inject
    EntityManager em;

    @Override
    @Transactional
    public void persistEntity(User entity) {
        this.em.persist(entity);
    }

    @Override
    public Optional<List<User>> findAllEntities() {

        TypedQuery<User> query = this.em.createQuery("FROM User", User.class);
        List<User> userList =  query.getResultList();

        if (userList.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(query.getResultList());
    }

    @Override
    public Optional<User> findEntityById(Long id) {
        Optional<User> user;

        user = Optional.ofNullable(this.em.find(User.class, id));

        return user;
    }

    @Override
    @Transactional
    public void updateEntity(User entity) {
        User oldUser = this.em.merge(entity);
    }


    @Override
    @Transactional
    public void removeEntity(Long id) {
        User user = this.em.find(User.class, id);
        this.em.remove(user);

    }
}
