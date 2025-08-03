package com.railse.workforcemgmt.dto;

import com.railse.workforcemgmt.model.TaskPriority;
import com.railse.workforcemgmt.model.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TaskDto {
    private String id;
    private String title;
    private String assignedTo;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate startDate;
}
