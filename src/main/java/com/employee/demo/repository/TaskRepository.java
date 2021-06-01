package com.employee.demo.repository;

import com.employee.demo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    void deleteTaskById(Long id);

}
