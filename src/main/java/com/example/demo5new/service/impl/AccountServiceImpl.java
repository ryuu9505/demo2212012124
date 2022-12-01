package com.example.demo5new.service.impl;

import com.example.demo5new.controller.form.AccountForm;
import com.example.demo5new.controller.form.AccountSaveForm;
import com.example.demo5new.domain.Role;
import com.example.demo5new.domain.user.Account;
import com.example.demo5new.repository.AccountRepository;
import com.example.demo5new.repository.RoleRepository;
import com.example.demo5new.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("accountService")
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    @Override
    public void saveAccount(AccountSaveForm accountSaveForm) {
        /*Account account = Account.builder()
                .username(accountSaveForm.getUsername())
                .email(accountSaveForm.getEmail())
                .password(passwordEncoder.encode(accountSaveForm.getPassword()))
                .build();*/

        accountSaveForm.setPassword(passwordEncoder.encode(accountSaveForm.getPassword()));
        Account account = modelMapper.map(accountSaveForm, Account.class);

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRoleName("ROLE_USER")); // todo exception handling
        account.setUserRoles(roles);
        accountRepository.save(account);
    }

    @Override
    public void modifyAccount(AccountForm accountForm) {
        Account account = modelMapper.map(accountForm, Account.class);
        if(accountForm.getRoles() != null){
            Set<Role> roles = new HashSet<>();
            accountForm.getRoles().forEach(role -> {
                roles.add(roleRepository.findByRoleName(role));
            });
            account.setUserRoles(roles);
        }
        account.setPassword(passwordEncoder.encode(accountForm.getPassword()));
        accountRepository.save(account);
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public AccountForm getAccount(Long id) {
        Account account = accountRepository.findById(id).orElse(new Account());
        AccountForm accountForm = modelMapper.map(account, AccountForm.class);

        List<String> roles = account.getUserRoles()
                .stream()
                .map(role -> role.getRoleName())
                .collect(Collectors.toList());

        accountForm.setRoles(roles);
        return accountForm;
    }

    @Override
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}
