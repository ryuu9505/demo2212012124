package com.example.demo5new.controller.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class AccountSaveForm {

    @Pattern(regexp = "^[a-z0-9_-]{3,20}$")
    private String username;

    @Email
    @NotBlank
    private String email;

    @Length(min = 8, max = 50)
    private String password;

}