package com.iwamotoraphael.todosimple.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iwamotoraphael.todosimple.models.Task;
import com.iwamotoraphael.todosimple.models.User;
import com.iwamotoraphael.todosimple.repositories.TaskRepository;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id){
        Optional<Task> task = this.taskRepository.findById(id);

        return task.orElseThrow(() -> new RuntimeException("Tarefa de id: "+id+" não foi encontrado."));
    }

    public List<Task> findAllByUserId(Long id){
        return this.taskRepository.findByUser_Id(id);   
    }

    @Transactional
    public Task create(Task task){
        User user = this.userService.findById(task.getUser().getId());

        task.setId(null);
        task.setUser(user);
        task = this.taskRepository.save(task);

        return task;
    }

    @Transactional
    public Task update(Task task){
        Task updatedTask = findById(task.getId());

        updatedTask.setDescription(task.getDescription());

        return this.taskRepository.save(updatedTask);
    }

    @Transactional
    public void delete(Long id){
        this.taskRepository.deleteById(id);
    }
}
