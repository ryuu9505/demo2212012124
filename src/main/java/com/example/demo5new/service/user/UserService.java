package com.example.demo5new.service.user;

import com.example.demo5new.common.authority.RoleMapper;
import com.example.demo5new.domain.user.Account;
import com.example.demo5new.domain.users.ProviderUser;
import com.example.demo5new.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final RoleMapper roleMapper;
    private final AccountRepository accountRepository;

    public void register(String registrationId, ProviderUser providerUser) {

        Account account = Account.builder()
                .registrationId(registrationId)
                .id(providerUser.getId())
                .username(providerUser.getUsername())
                .password(providerUser.getPassword())
                .userRoles(roleMapper.mapAuthorities(providerUser.getAuthorities()))
                .provider(providerUser.getProvider())
                .email(providerUser.getEmail())
                .build();

        accountRepository.save(account);
    }
}