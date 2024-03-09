package com.spring.login.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangeUsernameData {
    
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
}
