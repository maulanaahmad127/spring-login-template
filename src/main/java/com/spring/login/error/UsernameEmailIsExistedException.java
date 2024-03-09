package com.spring.login.error;

import org.springframework.dao.DataIntegrityViolationException;

public class UsernameEmailIsExistedException extends DataIntegrityViolationException {

    public UsernameEmailIsExistedException(String message) {
        super(message);
    }
    
}
