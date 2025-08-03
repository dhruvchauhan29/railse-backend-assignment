package com.railse.workforcemgmt.dto;

import com.railse.workforcemgmt.model.TaskPriority;
import com.railse.workforcemgmt.model.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class TaskDetailsDto {
    private String id;
    private String title;
    private String assignedTo;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate startDate;
    private List<String> comments;
    private List<String> activityLogs;
}
