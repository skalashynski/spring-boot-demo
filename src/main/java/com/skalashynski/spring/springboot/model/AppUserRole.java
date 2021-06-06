package com.skalashynski.spring.springboot.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.skalashynski.spring.springboot.model.AppUserPermission.*;

public enum AppUserRole {
    ADMIN(Set.of(COURSE_WRITE, COURSE_READ, STUDENT_WRITE, STUDENT_READ)),
    ADMIN_TRAINEE(Set.of(COURSE_READ, STUDENT_READ)),
    STUDENT(Set.of());

    private final Set<AppUserPermission> permissions;

    AppUserRole(Set<AppUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<AppUserPermission> getPermissions() {
        return permissions;
    }

    public Set<GrantedAuthority> getGrantedAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = getPermissions().stream().map(p -> new SimpleGrantedAuthority(p.getPermission())).collect(Collectors.toSet());
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return grantedAuthorities;
    }
}
