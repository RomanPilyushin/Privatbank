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
        // Use the task service to create a task
        Task createdTask = taskService.createTask(taskDTO);
        // Return the created task in the response
        return ResponseEntity.ok(createdTask);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete
     */
    @Operation(summary = "Delete a task", description = "Delete a task by its ID")
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        // Log the deletion of the task
        log.debug("Deleting task with ID: {}", id);
        // Use the task service to delete the task
        taskService.deleteTask(id);
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
        // Use the task service to update the task status
        return taskService.updateTaskStatus(id, status);
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
        // Use the task service to update task fields
        Task updatedTask = taskService.updateTaskFields(id, taskDTO);
        // Return the updated task in the response
        return ResponseEntity.ok(updatedTask);
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
        // Use the task service to get all tasks
        return taskService.getAllTasks();
    }

    /**
     * Generates and returns the RSS feed of tasks.
     *
     * @return String representation of the RSS feed in XML format
     * @throws Exception if an error occurs during feed generation
     */
    @GetMapping(value = "/rss", produces = MediaType.APPLICATION_XML_VALUE)
    public String getRssFeed() throws Exception {
        // Generate the RSS channel using the RSS feed service
        Channel channel = rssFeedService.generateFeed();
        // Create an output object for the feed
        WireFeedOutput output = new WireFeedOutput();
        // Return the feed as a string
        return output.outputString(channel);
    }
}
