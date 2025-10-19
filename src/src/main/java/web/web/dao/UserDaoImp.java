package web.dao;

import org.springframework.stereotype.Repository;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao{
    @PersistenceContext
    private EntityManager entityManager;

    public UserDaoImp() {}

    public UserDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<User> findAll() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    public User findById(Long id) {
        try {
            return entityManager.find(User.class, id);
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    public User save(User user) {
        try {
            if (user.getId() == null) {
                entityManager.persist(user);
                return user;
            } else {
                return entityManager.merge(user);
            }
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    public void delete(User user) {
        try {
            entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    public void deleteById(Long id) {
        User user = findById(id);
        if (user != null) {
            delete(user);
        }
    }
}
