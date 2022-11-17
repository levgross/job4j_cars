package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Driver;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class DriverRepository {
    private final CrudRepository crudRepository;
    /**
     * Save in database.
     * @param driver Driver
     * @return Driver with id.
     */
    public Optional<Driver> create(Driver driver) {
        try {
            crudRepository.run(session -> session.persist(driver));
            return Optional.of(driver);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Update driver in database.
     * @param driver Driver
     */
    public void update(Driver driver) {
        crudRepository.run(session -> session.merge(driver));
    }

    /**
     * Delete driver by id.
     * @param id Driver id
     */
    public void delete(int id) {
        crudRepository.run(
                "delete from Driver where id = :fId",
                Map.of("fId", id)
        );
    }

    /**
     * List of all drivers ordered by id.
     * @return list of drivers.
     */
    public List<Driver> findAllOrderById() {
        return crudRepository.query("from Driver order by id", Driver.class);
    }

    /**
     * Find driver by ID
     * @param id Driver id
     * @return Driver.
     */
    public Optional<Driver> findById(int id) {
        return crudRepository.optional(
                "from Driver d join fetch d.cars where d.id = :fId", Driver.class,
                Map.of("fId", id)
        );
    }
}
