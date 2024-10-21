package org.example.privatbank;

import org.example.privatbank.dto.TaskDTO;
import org.example.privatbank.model.Task;
import org.example.privatbank.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Task task;
    private TaskDTO taskDTO;

    @BeforeEach
    public void setup() {
        // Initialize test data
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("This is a test task");
        task.setStatus("Pending");

        taskDTO = new TaskDTO();
        taskDTO.setTitle("Test Task");
        taskDTO.setDescription("This is a test task");
        taskDTO.setStatus("Pending");
    }

    @Test
    public void testCreateTask() throws Exception {
        // Simulate task creation success
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("This is a test task"))
                .andExpect(jsonPath("$.status").value("Pending"));
    }

    @Test
    public void testCreateTaskDuplicate() throws Exception {
        // Simulate task with duplicate title
        when(taskService.createTask(any(TaskDTO.class))).thenThrow(new RuntimeException("Task with the same title already exists"));

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Task with the same title already exists"));
    }

    @Test
    public void testUpdateTaskStatusNotFound() throws Exception {
        // Simulate task not found
        when(taskService.updateTaskStatus(1L, "Completed")).thenThrow(new RuntimeException("Task not found"));

        mockMvc.perform(put("/api/tasks/1/status")
                        .param("status", "Completed"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Task not found"));
    }

    @Test
    public void testUpdateTaskFieldsNotFound() throws Exception {
        // Simulate task not found
        when(taskService.updateTaskFields(eq(1L), any(TaskDTO.class))).thenThrow(new RuntimeException("Task not found"));

        mockMvc.perform(patch("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Task not found"));
    }

    @Test
    public void testGetAllTasks() throws Exception {
        // Mock the service to return a list with the 'task'
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }

    @Test
    public void testUpdateTaskStatus() throws Exception {
        // Simulate that the task exists and status is updated successfully
        when(taskService.updateTaskStatus(1L, "Completed")).thenReturn(task);

        mockMvc.perform(put("/api/tasks/1/status")
                        .param("status", "Completed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Pending"));  // Adjust based on actual status in the returned task
    }

    @Test
    public void testUpdateTaskFields() throws Exception {
        // Simulate that the task exists and is updated successfully
        when(taskService.updateTaskFields(eq(1L), any(TaskDTO.class))).thenReturn(task);

        mockMvc.perform(patch("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.status").value("Pending"));
    }

    @Test
    public void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateTaskValidationError() throws Exception {
        TaskDTO invalidTaskDTO = new TaskDTO();
        invalidTaskDTO.setTitle("");
        invalidTaskDTO.setDescription("This is a test task");
        invalidTaskDTO.setStatus("");

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTaskDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title is mandatory"))
                .andExpect(jsonPath("$.status").value("Status is mandatory"));
    }
}
