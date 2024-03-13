package com.iwamotoraphael.todosimple.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iwamotoraphael.todosimple.models.Task;

public interface TaskRepository extends JpaRepository<Task,Long>{
    
    //Underscore makes the query look for the id within the user
    List<Task> findByUser_Id(Long id);

    void deleteByUser_Id(Long id);

    //Manual query with JPQL
    //@Query(value= "SELECT t FROM Task t WHERE t.user.id = :id")
    //List<Task> findByUserId(@Param("id") Long id);

    //Manual Query with SQL
    //@Query(value = "SELECT * FROM task t WHERE t.user_id = :id",nativeQuery = true)
    //List<Task> findByUserId(@Param("id") Long id);
}
