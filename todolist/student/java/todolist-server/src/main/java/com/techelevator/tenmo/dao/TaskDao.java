package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Task;

import java.util.Date;
import java.util.List;

public interface TaskDao {

    public List<Task> getAllTasks(int userId);
    public Task getTaskById(int taskId);
    public String addTask(String taskName, String taskDescription, Date dueDate, int accountId);
    public String updateTask(Task task, int taskStatusId);

}
