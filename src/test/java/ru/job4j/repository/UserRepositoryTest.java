package ru.job4j.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class UserRepositoryTest {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final UserRepository userRepository = new UserRepository(crudRepository);

    @AfterEach
    void tearDown() {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("DELETE FROM auto_user").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void whenCreateUserThenSameUser() {
        User user = new User();
        user.setLogin("login");
        user.setPassword("password");
        assertThat(userRepository.create(user)).isEqualTo(Optional.of(user));
    }

    @Test
    public void whenUpdateUserThenNewUser() {
        User user = new User();
        user.setLogin("user");
        user.setPassword("password");
        userRepository.create(user);
        user.setLogin("new user");
        user.setPassword("new password");
        userRepository.update(user);
        User result = userRepository.findById(user.getId()).get();
        assertThat(result.getLogin()).isEqualTo("new user");
        assertThat(result.getPassword()).isEqualTo("new password");
    }

    @Test
    public void whenDeleteUserThenNoUser() {
        User user = new User();
        user.setLogin("user");
        user.setPassword("password");
        userRepository.create(user);
        userRepository.delete(user.getId());
        assertThat(userRepository.findAllOrderById()).isEmpty();
    }

    @Test
    public void whenFindAllOrderById() {
        User user3 = new User();
        user3.setLogin("user3");
        user3.setPassword("password");
        User user2 = new User();
        user2.setLogin("user2");
        user2.setPassword("password");
        User user1 = new User();
        user1.setLogin("user1");
        user1.setPassword("password");
        userRepository.create(user3);
        userRepository.create(user1);
        userRepository.create(user2);
        assertThat(userRepository.findAllOrderById()).containsExactly(user3, user1, user2);
    }

    @Test
    public void whenFindByLogin() {
        User user3 = new User();
        user3.setLogin("user3");
        user3.setPassword("password");
        User user2 = new User();
        user2.setLogin("user2");
        user2.setPassword("password");
        User user1 = new User();
        user1.setLogin("user1");
        user1.setPassword("password");
        userRepository.create(user3);
        userRepository.create(user1);
        userRepository.create(user2);
        assertThat(userRepository.findByLogin("user2")).isEqualTo(Optional.of(user2));
    }

    @Test
    public void whenFindByLikeLogin() {
        User user3 = new User();
        user3.setLogin("login");
        user3.setPassword("password");
        User user2 = new User();
        user2.setLogin("user2");
        user2.setPassword("password");
        User user1 = new User();
        user1.setLogin("user1");
        user1.setPassword("password");
        userRepository.create(user3);
        userRepository.create(user1);
        userRepository.create(user2);
        assertThat(userRepository.findByLikeLogin("user")).contains(user2, user1);
    }
}


