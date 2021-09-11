package com.skalashynski.spring.springboot.service;

import com.skalashynski.spring.springboot.entity.AppUser;
import com.skalashynski.spring.springboot.exception.ApiException;

public interface AppUserService {

    String signUp(AppUser appUser) throws ApiException;
}
