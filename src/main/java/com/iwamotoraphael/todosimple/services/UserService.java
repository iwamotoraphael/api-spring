package com.iwamotoraphael.todosimple.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iwamotoraphael.todosimple.models.User;
import com.iwamotoraphael.todosimple.repositories.TaskRepository;
import com.iwamotoraphael.todosimple.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    public User findById(Long id){
        Optional<User> user = this.userRepository.findById(id);

        return user.orElseThrow(() -> new RuntimeException("Usuário de id: "+id+" não foi encontrado."));
    }

    @Transactional
    public User create(User obj){
        obj.setId(null);
        obj  = this.userRepository.save(obj);
        
        return obj;
    }

    @Transactional
    public User update(User obj){
        User updatedObj  = findById(obj.getId());
        updatedObj.setPassword(obj.getPassword());
        
        return this.userRepository.save(updatedObj);
    }

    @Transactional
    public void delete(Long id){
        this.taskRepository.deleteByUser_Id(id);
        this.userRepository.deleteById(id);
    }
}
 