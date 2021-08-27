package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JdbcTaskDao implements TaskDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private UserDao userDao;

    @Override
    public List<Task> getAllTasks(int userId) {
        List<Task> tasks = new ArrayList<>();
        return tasks;
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = new Task();
        return task;
    }

    @Override
    public String addTask(String taskName, String taskDescription, Date dueDate, int accountId) {
        return null;
    }

    @Override
    public String updateTask(Task task, int taskStatusId) {
        return null;
    }

}
