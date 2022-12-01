package com.example.demo5new.service.user;

import com.example.demo5new.certification.SelfCertification;
import com.example.demo5new.common.converters.ProviderUserConverter;
import com.example.demo5new.common.converters.ProviderUserRequest;
import com.example.demo5new.domain.users.PrincipalUser;
import com.example.demo5new.domain.users.ProviderUser;
import com.example.demo5new.repository.AccountRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomOAuth2UserService extends AbstractOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    public CustomOAuth2UserService(UserService userService, AccountRepository accountRepository, SelfCertification certification, ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter) {
        super(userService, accountRepository, certification, providerUserConverter);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        // get user info from authorization server
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration,oAuth2User);
        ProviderUser providerUser = providerUser(providerUserRequest);

        // 본인인증 체크
        // 기본은 본인인증을 하지 않은 상태임
        selfCertificate(providerUser);

        super.register(providerUser, userRequest);

        return new PrincipalUser(providerUser);
    }

}