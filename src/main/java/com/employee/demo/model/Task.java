package com.employee.demo.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String text;
    private String day;
    private boolean reminder;

}
