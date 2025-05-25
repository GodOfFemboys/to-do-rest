package org.example.todorest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TaskDto {
    private Long id;
    private String description;
    private Long user_id;
    private boolean completed;
}
