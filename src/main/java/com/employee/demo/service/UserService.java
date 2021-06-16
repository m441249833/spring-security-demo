package com.employee.demo.service;


import com.employee.demo.model.User;
import com.employee.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user){
        if (!userRepository.findByUsername(user.getUsername()).isPresent()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        else throw new RuntimeException("User already exist");
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User getUser(Long id){
        return userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found."));
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    public User addUser(User user){
        String username = user.getUsername();
        if (!userRepository.findByUsername(username).isPresent()) return userRepository.save(user);
        else return null;
    }

    public void deleteUser(Long id){
        userRepository.deleteUserById(id);
    }
}
