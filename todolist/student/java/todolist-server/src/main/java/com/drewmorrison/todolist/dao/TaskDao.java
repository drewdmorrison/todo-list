package com.drewmorrison.todolist.dao;

import com.drewmorrison.todolist.model.Account;
import com.drewmorrison.todolist.model.Task;

import java.util.Date;
import java.util.List;

public interface TaskDao {

    int getTasksLeft(int userId);
    List<Task> getAllTasks(int userId);
    Task getTaskById(int taskId);
    String addTask(String taskName, String taskDescription, Date dueDate, int userId);
    String updateTask(Task task, int taskStatusId);
    Account getAccountByUserId(int userId);
    int addToTasksRemaining(int userId);
    int removeFromTasksRemaining(int userId);


}
