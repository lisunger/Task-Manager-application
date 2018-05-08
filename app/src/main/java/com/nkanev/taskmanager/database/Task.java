package com.nkanev.taskmanager.database;

public class Task {

    private int id;
    private int categoryId;
    private String name;
    private String contents;
    private boolean complete;

    public Task() {

    }

    public Task(int id, int categoryId, String name, String contents, boolean complete) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.contents = contents;
        this.complete = complete;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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
