package org.example.privatbank.service;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Item;
import lombok.extern.slf4j.Slf4j;
import org.example.privatbank.model.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service for generating RSS feeds from tasks.
 */
@Slf4j
@Service
public class RssFeedService {

    /** List to store tasks for the RSS feed */
    private List<Task> tasks = new ArrayList<>();

    /**
     * Adds a task to the RSS feed.
     *
     * @param task the task to add
     */
    public void addTaskToFeed(Task task) {
        log.debug("Adding task to RSS feed: {}", task.getTitle());
        // Add the task to the tasks list
        tasks.add(task);
        log.info("Task '{}' added to RSS feed", task.getTitle());
    }

    /**
     * Generates the RSS feed channel from the tasks.
     *
     * @return the RSS feed channel
     */
    public Channel generateFeed() {
        log.debug("Generating RSS feed with {} tasks", tasks.size());
        // Create a new RSS channel
        Channel channel = new Channel();
        // Set the feed type to RSS 2.0
        channel.setFeedType("rss_2.0");
        // Set the title of the RSS feed
        channel.setTitle("Task Manager Feed");
        // Set the description of the RSS feed
        channel.setDescription("Latest tasks created");
        // Set the link for the RSS feed
        channel.setLink("http://localhost:8080/rss");

        // Create a list to hold RSS items
        List<Item> items = new ArrayList<>();
        // Loop through the tasks to create RSS items
        for (Task task : tasks) {
            log.debug("Creating RSS item for task: {}", task.getTitle());
            // Create a new RSS item
            Item item = new Item();
            // Set the title of the item to the task title
            item.setTitle(task.getTitle());
            // Set the author of the item
            item.setAuthor("Task Manager");
            // Set the publication date to the current date
            item.setPubDate(new Date());
            // Create a description for the item
            Description description = new Description();
            // Set the description value to the task description
            description.setValue(task.getDescription());
            // Assign the description to the item
            item.setDescription(description);
            // Add the item to the items list
            items.add(item);
            log.info("RSS item created for task: {}", task.getTitle());
        }

        // Set the items of the channel to the items list
        channel.setItems(items);
        log.info("RSS feed generated with {} items", items.size());
        // Return the generated RSS channel
        return channel;
    }
}
