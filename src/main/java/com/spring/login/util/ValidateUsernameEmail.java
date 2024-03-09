package com.spring.login.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.login.error.UsernameEmailIsExistedException;
import com.spring.login.repository.UserRepository;

@Service
public class ValidateUsernameEmail {

  @Autowired
  UserRepository userRepository;

    public void isUsernameExist(String username) throws UsernameEmailIsExistedException{
        if(userRepository.existsByUsername(username)){
            throw new UsernameEmailIsExistedException("username dengan nama :" + username + " sudah digunakan");
        }
    }

    public void isEmailExist(String email) throws UsernameEmailIsExistedException{
        if(userRepository.existsByEmail(email)){
            throw new UsernameEmailIsExistedException("email dengan nama :" + email + " sudah digunakan");
        }
    }
    
}
