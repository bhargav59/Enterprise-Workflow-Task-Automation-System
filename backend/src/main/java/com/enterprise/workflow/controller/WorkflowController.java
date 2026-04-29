package com.enterprise.workflow.controller;

import com.enterprise.workflow.entity.Workflow;
import com.enterprise.workflow.repository.WorkflowRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {
    private final WorkflowRepository workflowRepository;
    
    public WorkflowController(WorkflowRepository workflowRepository) {
        this.workflowRepository = workflowRepository;
    }
    
    @PostMapping
    public ResponseEntity<Workflow> create(@RequestBody Workflow workflow) {
        return ResponseEntity.ok(workflowRepository.save(workflow));
    }
    
    @GetMapping
    public ResponseEntity<List<Workflow>> getAll() {
        return ResponseEntity.ok(workflowRepository.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Workflow> getById(@PathVariable Long id) {
        return ResponseEntity.ok(workflowRepository.findById(id).orElseThrow());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Workflow> update(@PathVariable Long id, @RequestBody Workflow workflow) {
        workflow.setId(id);
        return ResponseEntity.ok(workflowRepository.save(workflow));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        workflowRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Workflow deleted"));
    }
}