package org.example.privatbank.service;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Item;
import org.example.privatbank.model.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RssFeedService {

    private List<Task> tasks = new ArrayList<>();

    public void addTaskToFeed(Task task) {
        tasks.add(task);
    }

    public Channel generateFeed() {
        Channel channel = new Channel();
        channel.setFeedType("rss_2.0");
        channel.setTitle("Task Manager Feed");
        channel.setDescription("Latest tasks created");
        channel.setLink("http://localhost:8080/rss");

        List<Item> items = new ArrayList<>();
        for (Task task : tasks) {
            Item item = new Item();
            item.setTitle(task.getTitle());
            item.setAuthor("Task Manager");
            item.setPubDate(new Date());
            Description description = new Description();
            description.setValue(task.getDescription());
            item.setDescription(description);
            items.add(item);
        }

        channel.setItems(items);
        return channel;
    }
}
