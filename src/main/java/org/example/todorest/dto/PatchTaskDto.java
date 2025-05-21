package org.example.todorest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PatchTaskDto {
    @NotBlank
    @Size(max = 255, message = "Description too long")
    private String description;
    @NotNull(message = "Completed can't be null")
    private boolean completed;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public PatchTaskDto() {
    }
}
