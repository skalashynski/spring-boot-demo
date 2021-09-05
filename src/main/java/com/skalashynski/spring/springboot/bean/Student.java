package com.skalashynski.spring.springboot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "students")
@Entity
public class Student {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    private Date birthday;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    public Student(Long id, String firstName, String lastName, LocalDate birthday, LocalDateTime createdAt) {
        this(firstName, lastName, birthday, createdAt);
        this.id = id;
    }

    public Student(String firstName, String lastName, LocalDate birthday, LocalDateTime createdAt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = java.util.Date.from(birthday.atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toInstant());
        this.createdAt = java.util.Date
            .from(createdAt.atZone(ZoneId.systemDefault())
                .toInstant());
    }
}


