package ru.job4j.model.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.job4j.model.User;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    private final SessionFactory sf;
    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return user;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                    "UPDATE User SET password = :uPwd WHERE id = :uId")
                    .setParameter("uPwd", user.getPassword())
                    .setParameter("uId", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(int userId) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "DELETE User WHERE id = :uId")
                    .setParameter("uId", userId)
                    .executeUpdate();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        Session session = sf.openSession();
        session.beginTransaction();
        Query<User> query = session.createQuery("FROM User ORDER BY id ASC");
        List<User> result = query.getResultList();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    /**
     * Найти пользователя по ID
     *
     * @return пользователь.
     */
    public Optional<User> findById(int userId) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query<User> query = session.createQuery("FROM User WHERE id = :uId");
        query.setParameter("uId", userId);
        Optional<User> result = query.uniqueResultOptional();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query<User> query = session.createQuery("FROM User WHERE login LIKE :uKey");
        query.setParameter("uKey", "%" + key + "%");
        List<User> result = query.getResultList();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query<User> query = session.createQuery("FROM User WHERE login = :uLgn");
        query.setParameter("uLgn", login);
        Optional<User> result = query.uniqueResultOptional();
        session.getTransaction().commit();
        session.close();
        return result;
    }
}
