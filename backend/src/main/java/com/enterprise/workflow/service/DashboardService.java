package com.enterprise.workflow.service;

import com.enterprise.workflow.dto.DashboardStats;
import com.enterprise.workflow.entity.Task;
import com.enterprise.workflow.repository.ProjectRepository;
import com.enterprise.workflow.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    
    public DashboardService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }
    
    public DashboardStats getStats() {
        List<Task> allTasks = taskRepository.findAll();
        long totalProjects = projectRepository.count();
        long totalTasks = allTasks.size();
        
        long completed = allTasks.stream().filter(t -> t.getStatus() == Task.TaskStatus.DONE).count();
        long inProgress = allTasks.stream().filter(t -> t.getStatus() == Task.TaskStatus.IN_PROGRESS).count();
        long pending = allTasks.stream().filter(t -> t.getStatus() == Task.TaskStatus.TODO).count();
        
        LocalDate today = LocalDate.now();
        long overdue = allTasks.stream()
                .filter(t -> t.getDeadline() != null && t.getDeadline().isBefore(today)
                        && t.getStatus() != Task.TaskStatus.DONE)
                .count();
        
        DashboardStats stats = new DashboardStats();
        stats.setTotalProjects(totalProjects);
        stats.setTotalTasks(totalTasks);
        stats.setCompletedTasks(completed);
        stats.setInProgressTasks(inProgress);
        stats.setPendingTasks(pending);
        stats.setOverdueTasks(overdue);
        
        List<DashboardStats.TaskSummary> summaries = new ArrayList<>();
        summaries.add(new DashboardStats.TaskSummary("TODO", pending));
        summaries.add(new DashboardStats.TaskSummary("IN_PROGRESS", inProgress));
        summaries.add(new DashboardStats.TaskSummary("DONE", completed));
        summaries.add(new DashboardStats.TaskSummary("OVERDUE", overdue));
        stats.setTasksByStatus(summaries);
        
        return stats;
    }
}