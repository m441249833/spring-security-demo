package com.employee.demo.service;


import com.employee.demo.exception.UserNotFoundException;
import com.employee.demo.model.Employee;
import com.employee.demo.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepo emoployeeRepo;

    @Autowired
    public EmployeeService(EmployeeRepo emoployeeRepo) {
        this.emoployeeRepo = emoployeeRepo;
    }


    public Employee addEmployee(Employee employee){
        employee.setEmployeeCode(UUID.randomUUID().toString());
        return emoployeeRepo.save(employee);
    }

    public List<Employee> findAllEmployee(){
        return emoployeeRepo.findAll();
    }

    public Employee updateEmployee(Employee employee){
        return emoployeeRepo.save(employee);
    }

    public Employee findEmployeeById(Long id){
        return emoployeeRepo.findEmployeeById(id).orElseThrow(() -> new UserNotFoundException("User not found."));
    }
    public void deleteEmployee(Long id){
        emoployeeRepo.deleteEmployeeById(id);
    }
}
