package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class PostRepository {
    private final CrudRepository crudRepository;

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
    public List<Post> findByName(int name) {
        return crudRepository.query(
                "from Post p join fetch p.car c where c.name = :fName",
                Post.class,
                Map.of("fName", name)
        );
    }
}
