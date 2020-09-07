package com.example.demo.bean;

import lombok.Data;


import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "STUDENT")
public class Student {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    private Date birthday;

}
