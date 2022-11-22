package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Post;
import ru.job4j.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PostRepository {
    private final CrudRepository crudRepository;

    /**
     * Save in database.
     * @param post Post.
     * @return Post with id.
     */
    public Optional<Post> create(Post post) {
        try {
            crudRepository.run(session -> session.persist(post));
            return Optional.of(post);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Update Post in database.
     * @param post Post.
     */
    public void update(Post post) {
        crudRepository.run(session -> session.merge(post));
    }

    /**
     * Delete Post by id.
     * @param postId ID
     */
    public void delete(int postId) {
        crudRepository.run(
                "delete from Post where id = :fId",
                Map.of("fId", postId)
        );
    }

    /**
     * Find all posts ordered by id.
     * @return list of posts.
     */
    public List<Post> findAllOrderById() {
        return crudRepository.query("from Post p join fetch p.car c join fetch c.engine order by p.id",
                Post.class
        );
    }

    /**
     * Find Post by id
     * @param postId ID
     * @return Post.
     */
    public Optional<Post> findById(int postId) {
        return crudRepository.optional(
                "from Post where id = :fId", Post.class,
                Map.of("fId", postId)
        );
    }

    /**
     * Find all posts created during the last day.
     * @return list of posts.
     */
    public List<Post> findCreatedLastDay() {
        return crudRepository.query(
                "from Post where created > :fTime",
                Post.class,
                Map.of("fTime", LocalDateTime.now().minusDays(1)));
    }

    /**
     * Find all posts with photo.
     * @return list of posts.
     */
    public List<Post> findAllWithPhoto() {
        return crudRepository.query(
                "from Post where photo is not null",
                Post.class);
    }

    /**
     * Find posts by car brand.
     * @param name Car brand
     * @return list of posts.
     */
    public List<Post> findByCarBrand(String name) {
        return crudRepository.query(
                "from Post where car.name = :fName",
                Post.class,
                Map.of("fName", name)
        );
    }
}
