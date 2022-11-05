package ru.job4j.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "auto_post")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;
    private String login;
    private String password;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany
    @JoinColumn(name = "post_id")
    private List<PriceHistory> priceHistory;
}
