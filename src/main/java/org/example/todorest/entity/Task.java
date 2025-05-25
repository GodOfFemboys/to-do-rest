package org.example.todorest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String description;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false)
    private boolean completed = false;

    public Task(String description, User user, boolean completed) {
        this.description = description;
        this.user = user;
        this.completed = completed;
    }

}
