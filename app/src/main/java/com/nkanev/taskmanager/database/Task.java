package com.nkanev.taskmanager.database;

public class Task {

    private int id;
    private int topicId;
    private String name;
    private String contents;
    private boolean complete;

    public Task() {

    }

    public Task(int id, int topicId, String name, String contents, boolean complete) {
        this.id = id;
        this.topicId = topicId;
        this.name = name;
        this.contents = contents;
        this.complete = complete;
    }

    public int getId() {
        return id;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
