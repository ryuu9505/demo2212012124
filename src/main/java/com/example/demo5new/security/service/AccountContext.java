package com.example.demo5new.security.service;

import com.example.demo5new.domain.user.Account;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
public class AccountContext extends org.springframework.security.core.userdetails.User {

    private final Account account;

    public AccountContext(Account account, List<GrantedAuthority> roles) {
        super(account.getUsername(), account.getPassword(), roles);
        this.account = account;
    }

}
