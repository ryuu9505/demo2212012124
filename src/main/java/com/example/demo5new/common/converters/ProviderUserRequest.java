package com.example.demo5new.common.converters;

import com.example.demo5new.domain.user.Account;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public record ProviderUserRequest (ClientRegistration clientRegistration, OAuth2User oAuth2User, Account account){
    public ProviderUserRequest(ClientRegistration clientRegistration, OAuth2User oAuth2User){
        this(clientRegistration,oAuth2User,null);
    };

    public ProviderUserRequest(Account account){
        this(null,null, account);
    };
}
