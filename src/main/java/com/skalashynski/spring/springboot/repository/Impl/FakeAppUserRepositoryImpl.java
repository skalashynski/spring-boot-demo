package com.skalashynski.spring.springboot.repository.Impl;

import com.skalashynski.spring.springboot.bean.AppUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.skalashynski.spring.springboot.model.AppUserRole.*;

@Repository
public class FakeAppUserRepositoryImpl {
    private final List<AppUser> APP_USERS;
    private final PasswordEncoder passwordEncoder;

    public FakeAppUserRepositoryImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        APP_USERS = new ArrayList<>(Arrays.asList(
                new AppUser(1L, "Linda", "Brown", "linda.yahoo.com", "linda",
                        passwordEncoder.encode("password123"), ADMIN, false, true),
                new AppUser(2L, "Tom", "Soer", "tom.yahoo.com", "tom",
                        passwordEncoder.encode("password123"), ADMIN_TRAINEE, false, true),
                new AppUser(3L, "Anna", "Smith", "asmith.yahoo.com", "annasmith",
                        passwordEncoder.encode("password"), STUDENT, false, true)
        ));
    }



    public Optional<AppUser> findByEmail(String email) {
        return APP_USERS.stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }

    public Optional<AppUser> findByUsername(String username) {
        return APP_USERS.stream().filter(u -> u.getUsername().equals(username)).findFirst();
    }

    public AppUser save(AppUser appUser) {
        appUser.setId((long) (APP_USERS.size() + 1));
        APP_USERS.add(appUser);
        return appUser;
    }
}
