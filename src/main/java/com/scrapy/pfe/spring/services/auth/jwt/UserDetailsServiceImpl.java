package com.scrapy.pfe.spring.services.auth.jwt;

import com.scrapy.pfe.spring.entities.User;
import com.scrapy.pfe.spring.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //write logic to get user from db
        Optional <User> optionalUser = userRepository.findFirstByEmail(email);
        if(optionalUser.isEmpty())throw new UsernameNotFoundException("User Not Found",null);
        return  new org.springframework.security.core.userdetails.User(optionalUser.get().getEmail(),optionalUser.get().getPassword(),new ArrayList<>());

    }
}