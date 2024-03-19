package com.iwamotoraphael.todosimple.services;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iwamotoraphael.todosimple.models.User;
import com.iwamotoraphael.todosimple.models.enums.ProfileEnum;
import com.iwamotoraphael.todosimple.repositories.TaskRepository;
import com.iwamotoraphael.todosimple.repositories.UserRepository;
import com.iwamotoraphael.todosimple.security.UserSpringSecurity;
import com.iwamotoraphael.todosimple.services.exceptions.AuthorizationException;
import com.iwamotoraphael.todosimple.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findById(Long id){
        UserSpringSecurity userSpringSecurity = authenticated();
        if(!userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId()))
            throw new AuthorizationException("Access denied.");

        Optional<User> user = this.userRepository.findById(id);

        return user.orElseThrow(() -> new ObjectNotFoundException("Usuário de id: "+id+" não foi encontrado."));
    }

    @Transactional
    public User create(User obj){
        obj.setId(null);
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet())); 
        obj  = this.userRepository.save(obj);
        
        return obj;
    }

    @Transactional
    public User update(User obj){
        User updatedObj  = findById(obj.getId());
        updatedObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        
        return this.userRepository.save(updatedObj);
    }

    @Transactional
    public void delete(Long id){
        findById(id);
        this.taskRepository.deleteByUser_Id(id);
        this.userRepository.deleteById(id);
    }

    public static UserSpringSecurity authenticated() {
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new AuthorizationException("Access denied.");
        }
    }
}
 