package ru.job4j.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "price_history")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;
    private long before;
    private long after;
    private LocalDateTime created;
}
