package com.iwamotoraphael.todosimple.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iwamotoraphael.todosimple.models.Task;
import com.iwamotoraphael.todosimple.models.User;
import com.iwamotoraphael.todosimple.models.enums.ProfileEnum;
import com.iwamotoraphael.todosimple.models.projections.TaskProjection;
import com.iwamotoraphael.todosimple.repositories.TaskRepository;
import com.iwamotoraphael.todosimple.security.UserSpringSecurity;
import com.iwamotoraphael.todosimple.services.exceptions.AuthorizationException;
import com.iwamotoraphael.todosimple.services.exceptions.ObjectNotFoundException;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id){

        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        Task task = this.taskRepository.findById(id)
        .orElseThrow(() ->  new ObjectNotFoundException("Task with id: "+id+" was not found."));

        if(!userHasTask(task, userSpringSecurity) && !userSpringSecurity.hasRole(ProfileEnum.ADMIN))
            throw new AuthorizationException("Access denied.");
    
        return task;
    }

    public List<TaskProjection> findAllByUser(){
        UserSpringSecurity userSpringSecurity = UserService.authenticated();

        return this.taskRepository.findByUser_Id(userSpringSecurity.getId());   
    }

    @Transactional
    public Task create(Task task){
        UserSpringSecurity userSpringSecurity = UserService.authenticated();

        User user = this.userService.findById(userSpringSecurity.getId());

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
        findById(id);
        this.taskRepository.deleteById(id);
    }

    public Boolean userHasTask(Task task, UserSpringSecurity userSpringSecurity) {
        return task.getUser().getId().equals(userSpringSecurity.getId());
    }
}
