package org.example.privatbank.model;

import jakarta.persistence.*; // Import for JPA annotations
import lombok.Data; // Import for Lombok's @Data annotation
import lombok.NoArgsConstructor; // Import for Lombok's @NoArgsConstructor

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
