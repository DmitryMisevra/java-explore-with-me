package ru.yandex.practicum.mainservice.event.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.mainservice.category.model.Category;
import ru.yandex.practicum.mainservice.user.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;

/**
 * Базовая сущность Event
 */
@Entity
@Table(name = "events")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraph(
        name = "event-with-initiator-and-category",
        attributeNodes = {
                @NamedAttributeNode("category"),
                @NamedAttributeNode("initiator")
        }
)
public class Event {

    @Column(name = "event_annotation", nullable = false)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Transient
    private Long confirmedRequests;

    @Column(name = "event_created", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "event_description", nullable = false)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @Transient
    private Location location;

    @Column(name = "event_paid", nullable = false)
    private Boolean paid;

    @Column(name = "event_participant_limit", nullable = false)
    private Long participantLimit;

    @Column(name = "event_published")
    private LocalDateTime publishedOn;

    @Column(name = "event_request_moderation", nullable = false)
    private Boolean requestModeration;

    @Column(name = "event_title", nullable = false)
    private String title;

    @Column(name = "event_state", nullable = false)
    private State state;

    @Transient
    private Long views;

    @PrePersist
    protected void onCreate() {
        if (createdOn == null) {
            createdOn = LocalDateTime.now();
        }
    }
}
