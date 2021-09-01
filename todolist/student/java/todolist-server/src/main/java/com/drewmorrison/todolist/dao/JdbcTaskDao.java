package com.drewmorrison.todolist.dao;

import com.drewmorrison.todolist.model.Account;
import com.drewmorrison.todolist.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JdbcTaskDao implements TaskDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TaskDao taskDao;

    @Override
    public int getTasksLeft(int userId) {
        int tasksLeft = 0;

        String sql = "SELECT task_count FROM accounts WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        if (results.next()) {
            tasksLeft = results.getInt("task_count");
        }

        return tasksLeft;
    }

    @Override
    public List<Task> getAllTasks(int userId) {
        List<Task> tasks = new ArrayList<>();

        String sql = "SELECT * FROM tasks " +
                     "JOIN accounts ON tasks.account_id = accounts.account_id " +
                     "JOIN users u ON accounts.user_id = u.user_id " +
                     "WHERE u.user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            Task task = mapRowToTask(results);
            tasks.add(task);
        }
        return tasks;
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = new Task();

        String sql = "SELECT t.*, ts.task_status_desc " +
                     "FROM tasks t " +
                     "JOIN accounts a ON t.account_id = a.account_id " +
                     "JOIN task_statuses ts ON t.task_status_id = ts.task_status_id " +
                     "WHERE t.task_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, taskId);
        if (results.next()) {
            task = mapRowToTask(results);
        }

        return task;
    }

    @Override
    public String addTask(String taskName, String taskDescription, Date dueDate, int userId) {
        int accountId = getAccountByUserId(userId).getAccountId();

        try {
            String sql = "INSERT INTO tasks(task_name, task_desc, due_date, task_status_id, account_id) " +
                         "VALUES(?, ?, ?, 1, ?);";
            jdbcTemplate.update(sql, taskName, taskDescription, dueDate, accountId);
        } catch (Exception ex) {
            return "**** Error. Failed to add task. ****";
        }
        addToTasksRemaining(userId);
        return "Task Added";
    }

    @Override
    public String updateTask(Task task, int taskStatusId) {
        if (taskStatusId == 2) {
            String sql = "UPDATE tasks SET task_status_id = ? WHERE task_id = ?;";
            jdbcTemplate.update(sql, taskStatusId, task.getTaskId());
            return "Task Updated";
        } else {
            return "Task Failed to Update";
        }
    }
    @Override
    public Account getAccountByUserId (int userId) {
        Account account = null;
        String sql = "SELECT * from accounts WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        if (results.next()) {
            account = mapRowToAccount(results);
        }

        return account;
    }

    @Override
    public int addToTasksRemaining(int userId) {
        Account account = getAccountByUserId(userId);
        int updatedTasksRemaining = account.getTaskCount() + 1;
        String sql = "UPDATE accounts SET task_count = ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, updatedTasksRemaining, account.getAccountId());

        return account.getTaskCount();
    }

    @Override
    public int removeFromTasksRemaining(int userId) {
        Account account = getAccountByUserId(userId);
        int updatedTasksRemaining = account.getTaskCount() - 1;
        String sql = "UPDATE accounts SET task_count = ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, updatedTasksRemaining, account.getAccountId());

        return account.getTaskCount();
    }

    public Task mapRowToTask(SqlRowSet results) {
        Task task = new Task();
        task.setTaskId(results.getInt("task_id"));
        task.setTaskName(results.getString("task_name"));
        task.setTaskDescription(results.getString("task_desc"));
        task.setDueDate(results.getDate("due_date"));
        task.setTaskStatusId(results.getInt("task_status_id"));
        task.setAccountId(results.getInt("account_id"));

        try {
            task.setTaskStatusDesc(results.getString("task_status_desc"));
        } catch (Exception ex) {}

        return task;
    }

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();
        account.setAccountId(results.getInt("account_id"));
        account.setUserId(results.getInt("user_id"));
        account.setTaskCount(results.getInt("task_count"));

        return account;
    }
}
