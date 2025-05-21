package org.example.todorest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.todorest.deserializer.BooleanStrictDeserializer;

public class PatchTaskDto {
    @NotBlank
    @Size(max = 255, message = "Description too long")
    private String description;
    @JsonDeserialize(using = BooleanStrictDeserializer.class)
    private Boolean completed;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public PatchTaskDto() {
    }
}
