package org.example.privatbank.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a task in the system.
 */
@Entity
@Table(name = "TASKS")
@Data
@NoArgsConstructor
public class Task {

    /** The unique identifier of the task */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The title of the task */
    private String title;

    /** The description of the task */
    private String description;

    /** The status of the task (e.g., "Pending", "In Progress", "Completed") */
    private String status;
}
