package com.example.demo5new.common.converters;

import com.example.demo5new.common.authority.RoleMapper;
import com.example.demo5new.domain.user.Account;
import com.example.demo5new.domain.users.ProviderUser;
import com.example.demo5new.domain.users.form.FormUser;
import org.springframework.beans.factory.annotation.Autowired;

public final class UserDetailsProviderUserConverter implements ProviderUserConverter<ProviderUserRequest,ProviderUser> {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {

        if(providerUserRequest.account() == null){
            return null;
        }

        Account account = providerUserRequest.account();
        return FormUser.builder()
                .id(account.getId())
                .username(account.getUsername())
                .password(account.getPassword())
                .authorities(roleMapper.mapRoles(account.getUserRoles()))
                .email(account.getEmail())
                .provider("none")
                .build();
    }
}
