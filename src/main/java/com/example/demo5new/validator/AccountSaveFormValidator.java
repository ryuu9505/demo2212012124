package com.example.demo5new.validator;

import com.example.demo5new.controller.form.AccountSaveForm;
import com.example.demo5new.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class AccountSaveFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountSaveForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountSaveForm accountSaveForm = (AccountSaveForm) target;
        if (accountRepository.existsByEmail(accountSaveForm.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{accountSaveForm.getEmail()}, "Invalid Email");
        }
        if (accountRepository.existsByUsername(accountSaveForm.getUsername())) {
            errors.rejectValue("username", "invalid.username", new Object[]{accountSaveForm.getUsername()}, "Invalid Username");
        }
    }
}