package com.example.demo5new.service;

import com.example.demo5new.controller.form.AccountForm;
import com.example.demo5new.controller.form.AccountSaveForm;
import com.example.demo5new.domain.user.Account;

import java.util.List;

public interface AccountService {

    void saveAccount(AccountSaveForm accountSaveForm);

    void modifyAccount(AccountForm accountForm);

    List<Account> getAccounts();

    AccountForm getAccount(Long id);

    void deleteAccount(Long id);
}
