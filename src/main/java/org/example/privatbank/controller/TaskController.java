package org.example.privatbank.controller;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.io.WireFeedOutput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.privatbank.dto.TaskDTO;
import org.example.privatbank.model.Task;
import org.example.privatbank.service.RssFeedService;
import org.example.privatbank.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing tasks.
 */
@Slf4j
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task API", description = "Operations related to tasks")
public class TaskController {

    /** Service for handling task operations */
    @Autowired
    private TaskService taskService;

    /** Service for generating RSS feeds */
    @Autowired
    private RssFeedService rssFeedService;

    /**
     * Creates a new task.
     *
     * @param taskDTO the task data transfer object
     * @return ResponseEntity containing the created task
     */
    @Operation(summary = "Create a new task", description = "Create a new task with the given details")
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        // Log the creation of a new task
        log.debug("Creating a new task: {}", taskDTO);
        try {
            Task createdTask = taskService.createTask(taskDTO);
            log.info("Task created successfully: {}", createdTask);
            return ResponseEntity.ok(createdTask);
        } catch (Exception e) {
            log.error("Error occurred while creating task: {}", taskDTO, e);
            throw e;
        }
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete
     */
    @Operation(summary = "Delete a task", description = "Delete a task by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        log.debug("Request received to delete task with ID: {}", id);

        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok("{\"message\": \"Task deleted successfully\"}");
        } catch (RuntimeException e) {
            log.error("Error while deleting task: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\": \"Task not found\"}");
        }
    }

    /**
     * Updates the status of a task.
     *
     * @param id     the ID of the task
     * @param status the new status
     * @return the updated task
     */
    @Operation(summary = "Update task status", description = "Update the status of a task")
    @PutMapping("/{id}/status")
    public Task updateTaskStatus(@PathVariable Long id, @RequestParam String status) {
        // Log the status update
        log.debug("Updating status of task with ID: {} to {}", id, status);
        try {
            Task updatedTask = taskService.updateTaskStatus(id, status);
            log.info("Task with ID {} updated to status {}", id, status);
            return updatedTask;
        } catch (Exception e) {
            log.error("Error occurred while updating status of task with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Updates specific fields of a task.
     *
     * @param id      the ID of the task
     * @param taskDTO the task data transfer object with updated fields
     * @return ResponseEntity containing the updated task
     */
    @Operation(summary = "Update task fields", description = "Update specific fields of a task")
    @PatchMapping("/{id}")
    public ResponseEntity<Task> updateTaskFields(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
        // Log the field update
        log.debug("Updating fields of task with ID: {}", id);
        try {
            Task updatedTask = taskService.updateTaskFields(id, taskDTO);
            log.info("Task with ID {} updated with new fields", id);
            return ResponseEntity.ok(updatedTask);
        } catch (Exception e) {
            log.error("Error occurred while updating fields of task with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Retrieves all tasks.
     *
     * @return List of all tasks
     */
    @Operation(summary = "Get list of tasks", description = "Retrieve a list of all tasks")
    @GetMapping
    public List<Task> getAllTasks() {
        // Log retrieval of tasks
        log.debug("Retrieving all tasks");
        try {
            List<Task> tasks = taskService.getAllTasks();
            log.info("Retrieved {} tasks", tasks.size());
            return tasks;
        } catch (Exception e) {
            log.error("Error occurred while retrieving tasks", e);
            throw e;
        }
    }

    /**
     * Generates and returns the RSS feed of tasks.
     *
     * @return String representation of the RSS feed in XML format
     * @throws Exception if an error occurs during feed generation
     */
    @GetMapping(value = "/rss", produces = MediaType.APPLICATION_XML_VALUE)
    public String getRssFeed() throws Exception {
        // Log RSS feed generation
        log.debug("Generating RSS feed");
        try {
            Channel channel = rssFeedService.generateFeed();
            WireFeedOutput output = new WireFeedOutput();
            String rssFeed = output.outputString(channel);
            log.info("RSS feed generated successfully");
            return rssFeed;
        } catch (Exception e) {
            log.error("Error occurred while generating RSS feed", e);
            throw e;
        }
    }
}
