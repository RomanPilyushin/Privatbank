package org.example.privatbank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object for Task.
 * Used for input validation and transferring data between layers.
 */
@Data
public class TaskDTO {

    /** The title of the task */
    @NotBlank(message = "Title is mandatory")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    /** The description of the task */
    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    /** The status of the task */
    @NotBlank(message = "Status is mandatory")
    private String status;
}
