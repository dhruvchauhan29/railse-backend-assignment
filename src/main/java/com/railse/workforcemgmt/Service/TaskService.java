package com.railse.workforcemgmt.Service;

import com.railse.workforcemgmt.dto.CreateTaskRequest;
import com.railse.workforcemgmt.model.Task;
import com.railse.workforcemgmt.model.TaskStatus;
import com.railse.workforcemgmt.model.TaskPriority;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final Map<String, Task> taskMap = new HashMap<>();

    public Task createTask(CreateTaskRequest request) {
        String id = UUID.randomUUID().toString();
        Task task = Task.builder()
                .id(id)
                .title(request.getTitle())
                .assignedTo(request.getAssignedTo())
                .priority(request.getPriority())
                .status(TaskStatus.PENDING)
                .startDate(LocalDate.now())
                .comments(new ArrayList<>())
                .activityLogs(new ArrayList<>(List.of("Task created at " + LocalDate.now())))
                .build();
        taskMap.put(id, task);
        return task;
    }

    public void reassignTask(String taskId, String newAssignee) {
        Task oldTask = taskMap.get(taskId);
        if (oldTask != null) {
            oldTask.setStatus(TaskStatus.CANCELLED);
            oldTask.getActivityLogs().add("Task reassigned to " + newAssignee + " on " + LocalDate.now());

            Task newTask = Task.builder()
                    .id(UUID.randomUUID().toString())
                    .title(oldTask.getTitle())
                    .assignedTo(newAssignee)
                    .priority(oldTask.getPriority())
                    .status(TaskStatus.PENDING)
                    .startDate(LocalDate.now())
                    .comments(new ArrayList<>())
                    .activityLogs(new ArrayList<>(List.of("Task reassigned from " + oldTask.getAssignedTo())))
                    .build();

            taskMap.put(newTask.getId(), newTask);
        }
    }

    public List<Task> getTasksByDateRange(LocalDate start, LocalDate end) {
        return taskMap.values().stream()
                .filter(task -> task.getStatus() != TaskStatus.CANCELLED)
                .filter(task -> !task.getStartDate().isBefore(start) && !task.getStartDate().isAfter(end))
                .collect(Collectors.toList());
    }

    public List<Task> getSmartTasksByDateRange(LocalDate start, LocalDate end) {
        return taskMap.values().stream()
                .filter(task -> task.getStatus() != TaskStatus.CANCELLED)
                .filter(task ->
                        (!task.getStartDate().isBefore(start) && !task.getStartDate().isAfter(end)) ||
                                (task.getStartDate().isBefore(start) && task.getStatus() != TaskStatus.COMPLETED))
                .collect(Collectors.toList());
    }

    public void updatePriority(String taskId, TaskPriority newPriority) {
        Task task = taskMap.get(taskId);
        if (task != null) {
            task.setPriority(newPriority);
            task.getActivityLogs().add("Priority updated to " + newPriority + " at " + LocalDate.now());
        }
    }

    public List<Task> getTasksByPriority(TaskPriority priority) {
        return taskMap.values().stream()
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());
    }

    public void addComment(String taskId, String comment) {
        Task task = taskMap.get(taskId);
        if (task != null) {
            task.getComments().add(comment);
            task.getActivityLogs().add("Comment added: " + comment + " at " + LocalDate.now());
        }
    }

    public Task getTaskById(String taskId) {
        return taskMap.get(taskId);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }
}
