package com.example.demo5new.common.converters;

import com.example.demo5new.common.enums.OAuth2Config;
import com.example.demo5new.common.util.OAuth2Utils;
import com.example.demo5new.domain.users.ProviderUser;
import com.example.demo5new.domain.users.social.NaverUser;

public final class OAuth2NaverProviderUserConverter implements ProviderUserConverter<ProviderUserRequest,ProviderUser> {
    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {

        if (!providerUserRequest.clientRegistration().getRegistrationId().equals(OAuth2Config.SocialType.NAVER.getSocialName())) {
            return null;
        }

        return new NaverUser(OAuth2Utils.getSubAttributes(
                providerUserRequest.oAuth2User(), "response"),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration());
    }
}
