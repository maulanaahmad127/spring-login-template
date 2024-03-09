package com.spring.login.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.login.models.User;
import com.spring.login.repository.UserRepository;
import com.spring.login.security.jwt.JwtUtils;

@Component
public class GetUsernameToken {
    
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserRepository userRepository;

    public String getUsernameStringFromToken (){
        String token = jwtUtils.getJwtFromCookies(request);
        String username = jwtUtils.getUserNameFromJwtToken(token);
        return username;
    }

    public User getUserNameFromToken(){
        String username = getUsernameStringFromToken();
        User user = userRepository.findByUsername(username).get();
        return user;
    }

}
