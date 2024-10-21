package org.example.privatbank;

import org.example.privatbank.model.Task;
import org.example.privatbank.repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @AfterEach
    public void tearDown() {
        taskRepository.deleteAll();
    }

    @Test
    public void testExistsByTitle() {
        Task task = new Task();
        task.setTitle("Unique Task");
        task.setDescription("Description");
        task.setStatus("Pending");

        taskRepository.save(task);

        boolean exists = taskRepository.existsByTitle("Unique Task");
        assertTrue(exists);

        boolean notExists = taskRepository.existsByTitle("Non-Existent Task");
        assertFalse(notExists);
    }

    @Test
    public void testSaveAndFindById() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Description");
        task.setStatus("Pending");

        Task savedTask = taskRepository.save(task);

        Task foundTask = taskRepository.findById(savedTask.getId()).orElse(null);

        assertNotNull(foundTask);
        assertEquals("Test Task", foundTask.getTitle());
    }
}
