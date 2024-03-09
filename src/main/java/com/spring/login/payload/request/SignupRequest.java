package com.spring.login.payload.request;

import java.util.Set;
import javax.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    private String nama;

    @NotBlank
    private String jenis_kelamin;

    @NotBlank
    private String no_handphone;

    @Size(max = 50)
    @Email
    @NotBlank
    private String email;

    @NotNull
    private Set<String> role;

    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    private String passwordConfirmation;

}
