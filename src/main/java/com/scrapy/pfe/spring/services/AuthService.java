package com.scrapy.pfe.spring.services;

import com.scrapy.pfe.spring.dtos.SignupRequest;
import com.scrapy.pfe.spring.dtos.UserDto;
import com.scrapy.pfe.spring.entities.User;

public interface AuthService {
   UserDto createUser(SignupRequest signupRequest);

}
