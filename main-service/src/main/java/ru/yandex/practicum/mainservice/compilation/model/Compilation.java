package ru.yandex.practicum.mainservice.compilation.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.mainservice.event.model.Event;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Базовая сущность Compilation
 */
@Entity
@Table(name = "compilations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {

    @ManyToMany
    @JoinTable(
            name = "compilations_events_collation",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id", nullable = false)
    private Long id;

    @Column(name = "compilation_pinned", nullable = false)
    private Boolean pinned;

    @Column(name = "compilation_title", nullable = false)
    private String title;

    public void updateWith(Compilation compilationToUpdate) {
        if (compilationToUpdate.getPinned() != null) {
            this.pinned = compilationToUpdate.getPinned();
        }
        if (compilationToUpdate.getTitle() != null) {
            this.title = compilationToUpdate.getTitle();
        }
    }
}
