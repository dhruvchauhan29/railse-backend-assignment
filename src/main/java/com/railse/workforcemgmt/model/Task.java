package com.railse.workforcemgmt.model;

import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    private String id;
    private String title;
    private String assignedTo;
    private LocalDate startDate;

    private TaskStatus status;
    private TaskPriority priority;

    @Builder.Default
    private List<String> comments = new ArrayList<>();

    @Builder.Default
    private List<String> activityLogs = new ArrayList<>();
}
