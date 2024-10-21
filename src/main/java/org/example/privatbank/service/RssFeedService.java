package org.example.privatbank.service;

import com.sun.syndication.feed.rss.Channel; // Import for RSS Channel
import com.sun.syndication.feed.rss.Description; // Import for RSS Description
import com.sun.syndication.feed.rss.Item; // Import for RSS Item
import org.example.privatbank.model.Task; // Import Task model
import org.springframework.stereotype.Service; // Import for Service annotation

import java.util.ArrayList; // Import for ArrayList
import java.util.Date; // Import for Date
import java.util.List; // Import for List

/**
 * Service for generating RSS feeds from tasks.
 */
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
        // Add the task to the tasks list
        tasks.add(task);
    }

    /**
     * Generates the RSS feed channel from the tasks.
     *
     * @return the RSS feed channel
     */
    public Channel generateFeed() {
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
        }

        // Set the items of the channel to the items list
        channel.setItems(items);
        // Return the generated RSS channel
        return channel;
    }
}
