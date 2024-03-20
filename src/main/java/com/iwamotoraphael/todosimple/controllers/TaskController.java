package com.iwamotoraphael.todosimple.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.iwamotoraphael.todosimple.models.Task;
import com.iwamotoraphael.todosimple.models.projections.TaskProjection;
import com.iwamotoraphael.todosimple.services.TaskService;

@RestController
@RequestMapping("/task")
@Validated
public class TaskController {
    
    @Autowired
    TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id){
        Task task = this.taskService.findById(id);

        return ResponseEntity.ok().body(task);
    }

    @GetMapping("/user")
    public ResponseEntity<List<TaskProjection>> findByUser(){
        List<TaskProjection> tasks = this.taskService.findAllByUser();

        return ResponseEntity.ok().body(tasks);
    }

    @PostMapping
    @Validated
    public ResponseEntity<Void> create(@Valid @RequestBody Task task){
        this.taskService.create(task);

        URI uri = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(task.getId())
        .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody Task task, @PathVariable Long id) {
        
        task.setId(id);
        this.taskService.update(task);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id) {
        
        this.taskService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
