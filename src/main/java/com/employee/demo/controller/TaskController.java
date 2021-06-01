package com.employee.demo.controller;


import com.employee.demo.model.Task;
import com.employee.demo.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks(){
        List<Task> allTask = taskService.getAllTask();
        return new ResponseEntity<>(allTask, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable("id") Long id){
        Task task = taskService.getTaskById(id);
        return new ResponseEntity<>(task,HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Task> addTask(@RequestBody Task task){
        Task newTask = taskService.addTask(task);
        return new ResponseEntity<>(newTask,HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Task> updateTask(@RequestBody Task task){
        Task updateTask = taskService.updateTask(task);
        return new ResponseEntity<>(updateTask, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTask(@RequestParam("id") Long id){
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
