package ru.yandex.practicum.statsserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Базовая сущность Hit
 */

@Entity
@Table(name = "hits")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hit_id", nullable = false)
    private Long id;
    @Column(name = "app_name", nullable = false)
    private String app;
    @Column(name = "app_uri", nullable = false)
    private String uri;
    @Column(name = "user_ip", nullable = false)
    private String ip;
    @Column(name = "hit_timestamp", nullable = false)
    private LocalDateTime timestamp;
}
