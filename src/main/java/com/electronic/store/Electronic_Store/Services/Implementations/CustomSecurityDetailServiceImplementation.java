package com.electronic.store.Electronic_Store.Services.Implementations;

import com.electronic.store.Electronic_Store.Repositories.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomSecurityDetailServiceImplementation implements UserDetailsService {

    @Autowired
    userRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(()-> new RuntimeException("No User Exist with Username: "+username));
    }
}
