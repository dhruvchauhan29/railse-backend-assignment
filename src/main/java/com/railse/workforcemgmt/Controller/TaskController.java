package com.railse.workforcemgmt.Controller;

import com.railse.workforcemgmt.dto.CreateTaskRequest;
import com.railse.workforcemgmt.dto.TaskDto;
import com.railse.workforcemgmt.dto.TaskDetailsDto;
import com.railse.workforcemgmt.model.Task;
import com.railse.workforcemgmt.model.TaskPriority;
import com.railse.workforcemgmt.model.TaskStatus;
import com.railse.workforcemgmt.Service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public TaskDto createTask(@RequestBody CreateTaskRequest request) {
        Task task = taskService.createTask(request);
        return toDto(task);
    }


    @PatchMapping("/{taskId}/reassign")
    public ResponseEntity<Void> reassignTask(
            @PathVariable String taskId,
            @RequestParam String newAssignee) {
        taskService.reassignTask(taskId, newAssignee);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/{taskId}/priority")
    public ResponseEntity<Void> updateTaskPriority(
            @PathVariable String taskId,
            @RequestParam TaskPriority priority) {
        taskService.updatePriority(taskId, priority);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{taskId}/comment")
    public ResponseEntity<Void> addCommentToTask(
            @PathVariable String taskId,
            @RequestParam String comment) {
        taskService.addComment(taskId, comment);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDetailsDto> getTaskDetailsById(@PathVariable String taskId) {
        Task task = taskService.getTaskById(taskId);
        if (task == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDetailsDto(task));
    }


    @GetMapping("/range")
    public List<TaskDto> getTasksByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return taskService.getTasksByDateRange(start, end).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    @GetMapping("/smart-range")
    public List<TaskDto> getSmartTasksByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return taskService.getSmartTasksByDateRange(start, end).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/priority/{priority}")
    public List<TaskDto> getTasksByPriority(@PathVariable TaskPriority priority) {
        return taskService.getTasksByPriority(priority).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private TaskDto toDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .assignedTo(task.getAssignedTo())
                .status(task.getStatus())
                .priority(task.getPriority())
                .startDate(task.getStartDate())
                .build();
    }

    private TaskDetailsDto toDetailsDto(Task task) {
        return TaskDetailsDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .assignedTo(task.getAssignedTo())
                .status(task.getStatus())
                .priority(task.getPriority())
                .startDate(task.getStartDate())
                .comments(task.getComments())
                .activityLogs(task.getActivityLogs())
                .build();
    }
}
