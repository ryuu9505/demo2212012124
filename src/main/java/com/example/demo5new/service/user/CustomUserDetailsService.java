package com.example.demo5new.service.user;

import com.example.demo5new.common.converters.ProviderUserConverter;
import com.example.demo5new.common.converters.ProviderUserRequest;
import com.example.demo5new.domain.Role;
import com.example.demo5new.domain.user.Account;
import com.example.demo5new.domain.users.PrincipalUser;
import com.example.demo5new.domain.users.ProviderUser;
import com.example.demo5new.repository.AccountRepository;
import com.example.demo5new.repository.RoleRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class CustomUserDetailsService extends AbstractOAuth2UserService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public CustomUserDetailsService(UserService userService, AccountRepository accountRepository, RoleRepository roleRepository, ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter) {
        super(userService, accountRepository, providerUserConverter);
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountRepository.findByUsername(username);

        if(account == null){
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByRoleName("ROLE_USER")); // todo exception handling
            account.setUserRoles(roles);

            account = Account.builder()
                    .id("1")
                    .username("onjsdnjs")
                    .password("{noop}1234")
                    .userRoles(roles)
                    .email("onjsdnjs@gmail.com")
                    .build();
        }

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(account);
        ProviderUser providerUser = providerUser(providerUserRequest);

        return new PrincipalUser(providerUser);
    }
}

