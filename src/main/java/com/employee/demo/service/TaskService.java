package com.employee.demo.service;

import com.employee.demo.model.Task;
import com.employee.demo.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService  {

    private final TaskRepository taskRepository;


    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTask(){
        return taskRepository.findAll();
    }
    public Task getTaskById(Long id){
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task Not Found"));
    }

    public Task addTask(Task task){
        return taskRepository.save(task);
    }

    public Task updateTask(Task task){
        return taskRepository.save(task);
    }

    public void deleteTask(Long id){
        taskRepository.deleteTaskById(id);
    }
}
