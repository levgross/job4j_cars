package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepository {
    private final CrudRepository crudRepository;
    /**
     * Save in database.
     * @param user User.
     * @return User with id.
     */
    public Optional<User> create(User user) {
        try {
            crudRepository.run(session -> session.persist(user));
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Update User in database.
     * @param user User.
     */
    public void update(User user) {
        crudRepository.run(session -> session.merge(user));
    }

    /**
     * Delete User by id.
     * @param userId ID
     */
    public void delete(int userId) {
        crudRepository.run(
                "delete from User where id = :fId",
                Map.of("fId", userId)
        );
    }

    /**
     * Find all users ordered by id.
     * @return list of users.
     */
    public List<User> findAllOrderById() {
        return crudRepository.query("from User order by id", User.class);
    }

    /**
     * Find User by id
     * @param userId ID
     * @return User.
     */
    public Optional<User> findById(int userId) {
        return crudRepository.optional(
                "from User where id = :fId", User.class,
                Map.of("fId", userId)
        );
    }

    /**
     * Find users by login LIKE %key%
     * @param key key
     * @return list of users.
     */
    public List<User> findByLikeLogin(String key) {
       return crudRepository.query(
               "from User where login like :fKey", User.class,
               Map.of("fKey", "%" + key + "%")
       );
    }

    /**
     * Find User by login.
     * @param login login.
     * @return User.
     */
    public Optional<User> findByLogin(String login) {
        return crudRepository.optional(
                "from User where login = :fLogin", User.class,
                Map.of("fLogin", login)
        );
    }
}
