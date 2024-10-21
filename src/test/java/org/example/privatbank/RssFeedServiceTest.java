package org.example.privatbank;

import com.sun.syndication.feed.rss.Channel;
import org.example.privatbank.model.Task;
import org.example.privatbank.service.RssFeedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RssFeedServiceTest {

    private RssFeedService rssFeedService;

    @BeforeEach
    public void setup() {
        rssFeedService = new RssFeedService();
    }

    @Test
    public void testAddTaskToFeedAndGenerateFeed() {
        // Create a new Task object and set its properties
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Task Description");
        task.setStatus("Pending");

        // Add the task to the RSS feed service
        rssFeedService.addTaskToFeed(task);

        // Generate the RSS feed
        Channel channel = rssFeedService.generateFeed();

        // Assert that the feed is not null and contains one item
        assertNotNull(channel);
        assertEquals(1, channel.getItems().size());

        // Cast the first item to RSS feed item and check its title
        com.sun.syndication.feed.rss.Item rssItem = (com.sun.syndication.feed.rss.Item) channel.getItems().get(0);
        assertEquals("Test Task", rssItem.getTitle());
        assertEquals("Task Description", rssItem.getDescription().getValue());
    }
}
