package org.example.privatbank.repository;

import org.example.privatbank.model.Task; // Import Task entity
import org.springframework.data.jpa.repository.JpaRepository; // Import JPA repository

/**
 * Repository interface for Task entity.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Checks if a task with the given title exists.
     *
     * @param title the title to check
     * @return true if a task with the title exists, false otherwise
     */
    boolean existsByTitle(String title);
}
