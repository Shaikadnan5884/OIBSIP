package com.example.to_do_app;

public class Task {
    private int id;
    private int userId;
    private String title;
    private String description;
    private String taskDate;
    private String taskTime;
    private String priority;
    private String category;
    private String timestamp;

    public Task(int id, int userId, String title, String description, String taskDate, String taskTime, String priority, String category, String timestamp) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.priority = priority;
        this.category = category;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getTaskDate() { return taskDate; }
    public String getTaskTime() { return taskTime; }
    public String getPriority() { return priority; }
    public String getCategory() { return category; }
    public String getTimestamp() { return timestamp; }
}
