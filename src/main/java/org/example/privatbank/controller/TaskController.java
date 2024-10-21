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

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task API", description = "Operations related to tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RssFeedService rssFeedService;

    // 1. Create task
    @Operation(summary = "Create a new task", description = "Create a new task with the given details")
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        log.debug("Creating a new task: {}", taskDTO);
        Task createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.ok(createdTask);
    }

    // 2. Delete task
    @Operation(summary = "Delete a task", description = "Delete a task by its ID")
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        log.debug("Deleting task with ID: {}", id);
        taskService.deleteTask(id);
    }

    // 3. Update task status
    @Operation(summary = "Update task status", description = "Update the status of a task")
    @PutMapping("/{id}/status")
    public Task updateTaskStatus(@PathVariable Long id, @RequestParam String status) {
        log.debug("Updating status of task with ID: {} to {}", id, status);
        return taskService.updateTaskStatus(id, status);
    }

    // 4. Update task fields
    @Operation(summary = "Update task fields", description = "Update specific fields of a task")
    @PatchMapping("/{id}")
    public ResponseEntity<Task> updateTaskFields(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
        log.debug("Updating fields of task with ID: {}", id);
        Task updatedTask = taskService.updateTaskFields(id, taskDTO);
        return ResponseEntity.ok(updatedTask);
    }

    // 5. Get list of tasks
    @Operation(summary = "Get list of tasks", description = "Retrieve a list of all tasks")
    @GetMapping
    public List<Task> getAllTasks() {
        log.debug("Retrieving all tasks");
        return taskService.getAllTasks();
    }

    @GetMapping(value = "/rss", produces = MediaType.APPLICATION_XML_VALUE)
    public String getRssFeed() throws Exception {
        Channel channel = rssFeedService.generateFeed();
        WireFeedOutput output = new WireFeedOutput();
        return output.outputString(channel);
    }
}
