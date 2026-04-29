package com.enterprise.workflow.service;

import com.enterprise.workflow.dto.ProjectRequest;
import com.enterprise.workflow.entity.Project;
import com.enterprise.workflow.entity.User;
import com.enterprise.workflow.repository.ProjectRepository;
import com.enterprise.workflow.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }
    
    public Project create(ProjectRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCreatedBy(user);
        return projectRepository.save(project);
    }
    
    public List<Project> getAll() {
        return projectRepository.findAll();
    }
    
    public Project getById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }
    
    public Project update(Long id, ProjectRequest request) {
        Project project = getById(id);
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        return projectRepository.save(project);
    }
    
    public void delete(Long id) {
        projectRepository.delete(getById(id));
    }
    
    public List<Project> getByUser(Long userId) {
        return projectRepository.findByCreatedById(userId);
    }
}