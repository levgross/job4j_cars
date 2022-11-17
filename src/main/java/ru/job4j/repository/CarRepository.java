package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CarRepository {
    private final CrudRepository crudRepository;
    /**
     * Save in database.
     * @param car car
     * @return car with id.
     */
    public Optional<Car> create(Car car) {
        try {
            crudRepository.run(session -> session.persist(car));
            return Optional.of(car);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Update in database.
     * @param car car
     */
    public void update(Car car) {
        crudRepository.run(session -> session.merge(car));
    }

    /**
     * Delete car by id.
     * @param carId ID
     */
    public void delete(int carId) {
        crudRepository.run(
                "delete from Car where id = :fId",
                Map.of("fId", carId)
        );
    }

    /**
     * List of all cars ordered by id.
     * @return list of cars.
     */
    public List<Car> findAllOrderById() {
        return crudRepository.query("from Car c join fetch c.engine order by c.id", Car.class);
    }

    /**
     * Find car by ID
     * @param carId ID
     * @return car.
     */
    public Optional<Car> findById(int carId) {
        return crudRepository.optional(
                "from Car c join fetch c.engine join fetch c.owners where c.id = :fId", Car.class,
                Map.of("fId", carId)
        );
    }

    /**
     * Find cars by name
     * @param name car name
     * @return list of cars.
     */
    public List<Car> findByName(int name) {
        return crudRepository.query(
                "from Car c join fetch c.engine where c.name = :fName", Car.class,
                Map.of("fName", name)
        );
    }
}
