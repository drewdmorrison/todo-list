package com.drewmorrison.todolist.services;

import com.drewmorrison.todolist.model.AuthenticatedUser;
import com.drewmorrison.todolist.model.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.rmi.server.ExportException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class TaskService {

    private String BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    public TaskService(String url, AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
        BASE_URL = url;
    }

    public int getTasksLeft() {
        int tasksLeft = 0;
        try {
            tasksLeft = restTemplate.exchange(BASE_URL + "user/tasks/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Integer.class).getBody();
            System.out.println("=========================");
            System.out.println("Tasks Remaining: " + tasksLeft);
            System.out.println("=========================");
        } catch (RestClientException ex) {
            ex.printStackTrace();
            System.out.println("**** Error get remaining tasks ****");
        }
        return tasksLeft;
    }

    public Task[] taskList() {
        Task[] tasks = null;
        try {
            tasks = restTemplate.exchange(BASE_URL + "alltasks/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Task[].class).getBody();
            System.out.println("=======================================================");
            System.out.println("Tasks");
            System.out.printf("%-12s %-30s %-12s", "ID", "Task Name", "Due Date");
            System.out.println();
            System.out.println("=======================================================");

            for (Task task : tasks) {
                System.out.printf("%-12s %-30s %-12s", task.getTaskId(), task.getTaskName(), task.getDueDate());
                System.out.println();
            }
            System.out.println("===================================================");
            System.out.println("Enter the Task Id to view its details (0 to cancel)");
            System.out.println("===================================================");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            int taskId = Integer.parseInt(input);
            if (taskId != 0) {
                for (Task task : tasks) {
                    if (taskId == task.getTaskId()) {
                        Task foundTask = restTemplate.exchange(BASE_URL + "tasks/" + task.getTaskId(), HttpMethod.GET, makeAuthEntity(), Task.class).getBody();
                        System.out.println("================================================");
                        System.out.println("Task Details");
                        System.out.println("================================================");
                        System.out.println("Task Id: " + foundTask.getTaskId());
                        System.out.println("Task Name: " + foundTask.getTaskName());
                        System.out.println("Task Description: " + foundTask.getTaskDescription());
                        System.out.println("Due Date: " + foundTask.getDueDate());
                        System.out.println("Task Status: " + foundTask.getTaskStatusDesc());
                        System.out.println("================================================");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("**** Error getting task list ****");
        }
        return tasks;
    }

    public void addTask() {
        Task task = new Task();

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("==============================");
            System.out.print("Enter Task Name:");
            String taskNameInput = scanner.nextLine();
            task.setTaskName(taskNameInput);

            System.out.println("==============================");
            System.out.print("Enter Task Description: ");
            String taskDescInput = scanner.nextLine();
            task.setTaskDescription(taskDescInput);

            System.out.println("==============================");
            System.out.print("Enter Due Date: ");
            Date dateInput = new SimpleDateFormat("MM/dd/yyyy").parse(scanner.nextLine());
            task.setDueDate(dateInput);
            task.setAccountId(currentUser.getUser().getId());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(currentUser.getToken());
            HttpEntity entity2 = new HttpEntity(task, headers);

            String results = restTemplate.exchange(BASE_URL + "tasks/add",HttpMethod.POST, entity2, String.class).getBody();
            System.out.println(results);

        } catch (Exception ex) {
            System.out.println("**** Error Adding Task ****");
        }

    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity(headers);
        return entity;
    }
}
