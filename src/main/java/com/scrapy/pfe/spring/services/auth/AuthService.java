package com.scrapy.pfe.spring.services.auth;

import com.scrapy.pfe.spring.dtos.SignupRequest;
import com.scrapy.pfe.spring.dtos.UserDto;
import com.scrapy.pfe.spring.entities.User;

public interface AuthService {
   UserDto createUser(SignupRequest signupRequest);

}
