package com.example.pro_q;

public class TaskModel {
    String taskID, taskTitle;

    public TaskModel(String taskID, String taskTitle) {
        this.taskID = taskID;
        this.taskTitle = taskTitle;
    }

    public String getTaskID() {
        return taskID;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "taskID='" + taskID + '\'' +
                ", taskTitle='" + taskTitle + '\'' +
                '}';
    }
}
