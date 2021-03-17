package ru.vohmin.dao;

import org.springframework.stereotype.Repository;
import ru.vohmin.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {
    @PersistenceContext
    EntityManager em;

    @Override
    public void add(User user) {
        em.persist(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        return (List<User>) em.createQuery("from User").getResultList();
    }

    @Override
    public void remove(Long id) {
        User user = em.find(User.class, id);
        em.remove(user);
    }

    @Override
    public User findUser(Long id) {
        return em.find(User.class, id);
    }

    @Override
    public void updateUser(User user) {
        em.merge(user);
    }
}
