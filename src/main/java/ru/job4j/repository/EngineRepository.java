package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Engine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class EngineRepository {
    private final CrudRepository crudRepository;
    /**
     * Save in database.
     * @param engine Engine
     * @return Engine with id.
     */
    public Optional<Engine> create(Engine engine) {
        try {
            crudRepository.run(session -> session.persist(engine));
            return Optional.of(engine);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Update engine in database.
     * @param engine Engine
     */
    public void update(Engine engine) {
        crudRepository.run(session -> session.merge(engine));
    }

    /**
     * Delete engine by id.
     * @param id Engine ID
     */
    public void delete(int id) {
        crudRepository.run(
                "delete from Engine where id = :fId",
                Map.of("fId", id)
        );
    }

    /**
     * List of all engines ordered by id.
     * @return List of engines.
     */
    public List<Engine> findAllOrderById() {
        return crudRepository.query("from Engine order by id", Engine.class);
    }

    /**
     * Find engine by ID
     * @param id Engine ID
     * @return car.
     */
    public Optional<Engine> findById(int id) {
        return crudRepository.optional(
                "from Engine c join where id = :fId", Engine.class,
                Map.of("fId", id)
        );
    }
}
