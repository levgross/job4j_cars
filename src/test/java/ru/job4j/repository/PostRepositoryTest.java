package ru.job4j.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.model.Car;
import ru.job4j.model.Engine;
import ru.job4j.model.Post;
import ru.job4j.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class PostRepositoryTest {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final PostRepository postRepository = new PostRepository(crudRepository);
    private final UserRepository userRepository = new UserRepository(crudRepository);
    private final CarRepository carRepository = new CarRepository(crudRepository);
    private final EngineRepository engineRepository = new EngineRepository(crudRepository);

    @AfterEach
    void tearDown() {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("DELETE FROM auto_post").executeUpdate();
            session.createSQLQuery("DELETE FROM auto_user").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void findCreatedLastDay() {
        User user = new User();
        user.setLogin("login");
        user.setPassword("password");
        userRepository.create(user);
        Engine engine = new Engine();
        engine.setName("2.0 Diesel");
        engineRepository.create(engine);
        Car car1 = new Car();
        car1.setName("VW");
        car1.setEngine(engine);
        carRepository.create(car1);
        Car car2 = new Car();
        car2.setName("VW");
        car2.setEngine(engine);
        carRepository.create(car2);
        Car car3 = new Car();
        car3.setName("VW");
        car3.setEngine(engine);
        carRepository.create(car3);
        Post post1 = new Post();
        post1.setCar(car1);
        post1.setCreated(LocalDateTime.now().minusDays(2));
        post1.setUser(user);
        postRepository.create(post1);
        Post post2 = new Post();
        post2.setCar(car2);
        post2.setCreated(LocalDateTime.now().minusHours(2));
        post2.setUser(user);
        postRepository.create(post2);
        Post post3 = new Post();
        post3.setCar(car3);
        post3.setCreated(LocalDateTime.now());
        post3.setUser(user);
        postRepository.create(post3);
        assertThat(postRepository.findCreatedLastDay()).containsExactly(post2, post3);
        assertThat(postRepository.findCreatedLastDay()).doesNotContain(post1);
    }

    @Test
    void findAllWithPhoto() {
        User user = new User();
        user.setLogin("login");
        user.setPassword("password");
        userRepository.create(user);
        Engine engine = new Engine();
        engine.setName("2.0 Diesel");
        engineRepository.create(engine);
        Car car1 = new Car();
        car1.setName("VW");
        car1.setEngine(engine);
        carRepository.create(car1);
        Car car2 = new Car();
        car2.setName("VW");
        car2.setEngine(engine);
        carRepository.create(car2);
        Car car3 = new Car();
        car3.setName("VW");
        car3.setEngine(engine);
        carRepository.create(car3);
        byte[] photo = "photo".getBytes();
        Post post1 = new Post();
        post1.setUser(user);
        post1.setCar(car1);
        postRepository.create(post1);
        Post post2 = new Post();
        post2.setPhoto(photo);
        post2.setUser(user);
        post2.setCar(car2);
        postRepository.create(post2);
        Post post3 = new Post();
        post3.setPhoto(photo);
        post3.setUser(user);
        post3.setCar(car3);
        postRepository.create(post3);
        assertThat(postRepository.findAllWithPhoto()).contains(post2, post3);
        assertThat(postRepository.findAllWithPhoto()).doesNotContain(post1);
    }

    @Test
    void findByCarBrand() {
        User user = new User();
        user.setLogin("login");
        user.setPassword("password");
        userRepository.create(user);
        Engine engine = new Engine();
        engine.setName("2.0 Diesel");
        engineRepository.create(engine);
        Car vw = new Car();
        vw.setName("VW");
        vw.setEngine(engine);
        carRepository.create(vw);
        Car bmw = new Car();
        bmw.setName("BMW");
        bmw.setEngine(engine);
        carRepository.create(bmw);
        Post postVW = new Post();
        postVW.setUser(user);
        postVW.setText("text");
        postVW.setCar(vw);
        postRepository.create(postVW);
        Post postBMW = new Post();
        postBMW.setUser(user);
        postBMW.setCar(bmw);
        postRepository.create(postBMW);
        assertThat(postRepository.findAllOrderById()).containsExactly(postVW, postBMW);
//        assertThat(postRepository.findByCarBrand("BMW")).containsExactly(postBMW);
    }
}