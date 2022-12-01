package com.example.demo5new.service.user;

import com.example.demo5new.certification.SelfCertification;
import com.example.demo5new.common.converters.ProviderUserConverter;
import com.example.demo5new.common.converters.ProviderUserRequest;
import com.example.demo5new.domain.users.PrincipalUser;
import com.example.demo5new.domain.users.ProviderUser;
import com.example.demo5new.repository.AccountRepository;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomOidcUserService extends AbstractOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    public CustomOidcUserService(UserService userService, AccountRepository accountRepository, SelfCertification certification, ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter) {
        super(userService, accountRepository, certification, providerUserConverter);
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        // Open ID Connect 인 경우 User name Attribute Key 가 sub 이기 때문에 재정의함
        ClientRegistration clientRegistration = ClientRegistration
                                                .withClientRegistration(userRequest.getClientRegistration())
                                                .userNameAttributeName("sub")
                                                .build();
        OidcUserRequest oidcUserRequest =
                new OidcUserRequest(clientRegistration, userRequest.getAccessToken(),
                        userRequest.getIdToken(), userRequest.getAdditionalParameters());

        OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService = new OidcUserService();
        OidcUser oidcUser = oidcUserService.loadUser(oidcUserRequest);

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration,oidcUser);
        ProviderUser providerUser = providerUser(providerUserRequest);

        selfCertificate(providerUser);

        super.register(providerUser, oidcUserRequest);

        return new PrincipalUser(providerUser);
    }
}
