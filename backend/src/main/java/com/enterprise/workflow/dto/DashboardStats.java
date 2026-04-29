package com.enterprise.workflow.dto;

import java.util.List;

public class DashboardStats {
    private long totalProjects;
    private long totalTasks;
    private long completedTasks;
    private long overdueTasks;
    private long pendingTasks;
    private long inProgressTasks;
    private List<TaskSummary> tasksByStatus;
    
    public long getTotalProjects() { return totalProjects; }
    public void setTotalProjects(long totalProjects) { this.totalProjects = totalProjects; }
    public long getTotalTasks() { return totalTasks; }
    public void setTotalTasks(long totalTasks) { this.totalTasks = totalTasks; }
    public long getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(long completedTasks) { this.completedTasks = completedTasks; }
    public long getOverdueTasks() { return overdueTasks; }
    public void setOverdueTasks(long overdueTasks) { this.overdueTasks = overdueTasks; }
    public long getPendingTasks() { return pendingTasks; }
    public void setPendingTasks(long pendingTasks) { this.pendingTasks = pendingTasks; }
    public long getInProgressTasks() { return inProgressTasks; }
    public void setInProgressTasks(long inProgressTasks) { this.inProgressTasks = inProgressTasks; }
    public List<TaskSummary> getTasksByStatus() { return tasksByStatus; }
    public void setTasksByStatus(List<TaskSummary> tasksByStatus) { this.tasksByStatus = tasksByStatus; }
    
    public static class TaskSummary {
        private String status;
        private long count;
        
        public TaskSummary() {}
        public TaskSummary(String status, long count) {
            this.status = status;
            this.count = count;
        }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }
    }
}