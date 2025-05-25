package org.example.todorest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.todorest.deserializer.BooleanStrictDeserializer;

@Getter
@Setter
@NoArgsConstructor
public class PatchTaskDto {
    @Size(max = 255, message = "Description too long")
    private String description;
    @JsonDeserialize(using = BooleanStrictDeserializer.class)
    private Boolean completed;
}
