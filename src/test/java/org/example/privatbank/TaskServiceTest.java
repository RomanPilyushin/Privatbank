package org.example.privatbank;

import org.example.privatbank.dto.TaskDTO;
import org.example.privatbank.model.Task;
import org.example.privatbank.repository.TaskRepository;
import org.example.privatbank.service.RssFeedService;
import org.example.privatbank.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private RssFeedService rssFeedService;

    @InjectMocks
    private TaskService taskService;

    private TaskDTO taskDTO;
    private Task task;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        taskDTO = new TaskDTO();
        taskDTO.setTitle("Test Task");
        taskDTO.setDescription("This is a test task");
        taskDTO.setStatus("Pending");

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("This is a test task");
        task.setStatus("Pending");
    }

    @Test
    public void testCreateTaskSuccess() {
        when(taskRepository.count()).thenReturn(50L);
        when(taskRepository.existsByTitle("Test Task")).thenReturn(false);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(taskDTO);

        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());
        verify(rssFeedService, times(1)).addTaskToFeed(createdTask);
    }

    @Test
    public void testCreateTaskDuplicateTitle() {
        when(taskRepository.count()).thenReturn(50L);
        when(taskRepository.existsByTitle("Test Task")).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.createTask(taskDTO);
        });

        assertEquals("Task with the same title already exists", exception.getMessage());
    }

    @Test
    public void testCreateTaskLimitReached() {
        when(taskRepository.count()).thenReturn(100L);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.createTask(taskDTO);
        });

        assertEquals("Task limit reached. Cannot create more tasks.", exception.getMessage());
    }

    @Test
    public void testUpdateTaskStatus() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateTaskStatus(1L, "Completed");

        assertEquals("Completed", updatedTask.getStatus());
    }

    @Test
    public void testUpdateTaskStatusTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.updateTaskStatus(1L, "Completed");
        });

        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    public void testDeleteTask() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdateTaskFields() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO updateDTO = new TaskDTO();
        updateDTO.setTitle("Updated Task");
        updateDTO.setDescription("Updated Description");
        updateDTO.setStatus("In Progress");

        Task updatedTask = taskService.updateTaskFields(1L, updateDTO);

        assertEquals("Updated Task", updatedTask.getTitle());
        assertEquals("Updated Description", updatedTask.getDescription());
        assertEquals("In Progress", updatedTask.getStatus());
    }
}
