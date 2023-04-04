package com.example.pro_q;

public class TaskModel {
    String name, description, category;
    public TaskModel() {}
    @Override
    public String toString() {
        return "TaskModel {" +
                "taskName = '" + name + '\'' +
                ", taskDesc = '" + description + '\'' +
                ", taskCategory = '" + category + '\'' +
                '}';
    }

    public String getTaskName() {
        return name;
    }

    public String getTaskDesc() {
        return description;
    }

    public String getTaskCategory() {
        return category;
    }

    public TaskModel(String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }
}
