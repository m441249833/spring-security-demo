package com.employee.demo.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false)
    private Long id;
    private String name;
}
