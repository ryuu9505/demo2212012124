package com.example.demo5new.service.user;

import com.example.demo5new.common.converters.ProviderUserConverter;
import com.example.demo5new.common.converters.ProviderUserRequest;
import com.example.demo5new.domain.user.Account;
import com.example.demo5new.domain.users.ProviderUser;
import com.example.demo5new.repository.AccountRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

@Service
@Getter
@RequiredArgsConstructor
public abstract class AbstractOAuth2UserService {

    private final UserService userService;
    private final AccountRepository accountRepository;
    private final ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter;

    public void register(ProviderUser providerUser, OAuth2UserRequest userRequest){

        Account account = accountRepository.findByUsername(providerUser.getUsername());

        if(account == null){
            ClientRegistration clientRegistration = userRequest.getClientRegistration();
            userService.register(clientRegistration.getRegistrationId(),providerUser);
        }else{
            System.out.println("userRequest = " + userRequest);
        }
    }

    public ProviderUser providerUser(ProviderUserRequest providerUserRequest){
        return providerUserConverter.convert(providerUserRequest);
    }
}
