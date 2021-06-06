package com.skalashynski.spring.springboot.model;

import java.util.Set;

import static com.skalashynski.spring.springboot.model.AppUserPermission.*;

public enum AppUserRole {
    ADMIN(Set.of(COURSE_WRITE, COURSE_READ, STUDENT_WRITE, STUDENT_READ)),
    STUDENT(Set.of());

    private final Set<AppUserPermission> permissions;

    AppUserRole(Set<AppUserPermission> permissions) {
        this.permissions = permissions;
    }
}
