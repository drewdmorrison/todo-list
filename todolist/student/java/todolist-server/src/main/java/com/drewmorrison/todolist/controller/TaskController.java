package com.drewmorrison.todolist.controller;

import com.drewmorrison.todolist.dao.TaskDao;
import com.drewmorrison.todolist.dao.UserDao;
import com.drewmorrison.todolist.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TaskController {

    @Autowired
    private TaskDao taskDao;
    @Autowired
    private UserDao userDao;


    @RequestMapping(path = "user/tasks/{userId}", method = RequestMethod.GET)
    public int getTasksLeft(@PathVariable int userId) {
        return taskDao.getTasksLeft(userId);
    }

    @RequestMapping(path = "/alltasks/{userId}", method = RequestMethod.GET)
    public List<Task> getAllTasks(@PathVariable int userId) {
        return taskDao.getAllTasks(userId);
    }

    @RequestMapping(path = "/tasks/{taskId}", method = RequestMethod.GET)
    public Task getTaskById(@PathVariable int taskId) {
        return taskDao.getTaskById(taskId);
    }

    @RequestMapping(path = "/tasks/add", method = RequestMethod.POST)
    public String addTask(@RequestBody Task task) {
        return taskDao.addTask(task.getTaskName(), task.getTaskDescription(), task.getDueDate(), task.getAccountId());
    }

    @RequestMapping(path = "/tasks/update/{taskId}", method = RequestMethod.PUT)
    public String updateTask(@RequestBody Task task, @PathVariable int taskId) {
        return taskDao.updateTask(task, taskId);
    }
}
