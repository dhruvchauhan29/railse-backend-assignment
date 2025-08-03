package com.railse.workforcemgmt.dto;

import com.railse.workforcemgmt.model.TaskPriority;
import lombok.Data;

@Data
public class CreateTaskRequest {
    private String title;
    private String assignedTo;
    private TaskPriority priority;
}
