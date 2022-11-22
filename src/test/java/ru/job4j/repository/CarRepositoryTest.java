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
import ru.job4j.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class CarRepositoryTest {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final CarRepository carRepository = new CarRepository(crudRepository);
    private final EngineRepository engineRepository = new EngineRepository(crudRepository);

    @AfterEach
    void tearDown() {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("DELETE FROM car").executeUpdate();
            session.createSQLQuery("DELETE FROM engine").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void whenCreateCarThenSameCar() {
        Engine engine = new Engine();
        engine.setName("engine");
        engineRepository.create(engine);
        Car car = new Car();
        car.setName("car");
        car.setEngine(engine);
        assertThat(carRepository.create(car)).isPresent();
        assertThat(carRepository.findById(car.getId())).isEqualTo(Optional.of(car));
        assertThat(carRepository.findByName("car")).containsExactly(car);

    }

    @Test
    public void whenUpdateCarThenNewCar() {
        Engine engine = new Engine();
        engine.setName("engine");
        engineRepository.create(engine);
        Engine newEngine = new Engine();
        newEngine.setName("new engine");
        engineRepository.create(newEngine);
        Car car = new Car();
        car.setName("car");
        car.setEngine(engine);
        carRepository.create(car);
        car.setName("new car");
        car.setEngine(newEngine);
        carRepository.update(car);
        Car result = carRepository.findById(car.getId()).get();
        assertThat(result.getName()).isEqualTo("new car");
        assertThat(result.getEngine()).isEqualTo(newEngine);
        assertThat(result.getEngine().getName()).isEqualTo("new engine");
    }

    @Test
    public void whenDeleteCarThenNoCar() {
        Engine engine = new Engine();
        engine.setName("engine");
        engineRepository.create(engine);
        Car car = new Car();
        car.setName("car");
        car.setEngine(engine);
        carRepository.create(car);
        carRepository.delete(car.getId());
        assertThat(carRepository.findAllOrderById()).isEmpty();
    }

    @Test
    void whenFindAllOrderById() {
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
        assertThat(carRepository.findAllOrderById()).containsExactly(car1, car2, car3);
    }
}