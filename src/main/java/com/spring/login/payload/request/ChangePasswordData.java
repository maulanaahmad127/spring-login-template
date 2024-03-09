package com.spring.login.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordData {

    @NotBlank
    private String passwordLama;

    @Size(min = 6, max = 40)
    private String passwordBaru;

    @NotBlank
    private String passwordBaruConfirmation;

}
