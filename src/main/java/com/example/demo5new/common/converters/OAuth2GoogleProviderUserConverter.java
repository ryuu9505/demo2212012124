package com.example.demo5new.common.converters;

import com.example.demo5new.common.enums.OAuth2Config;
import com.example.demo5new.common.util.OAuth2Utils;
import com.example.demo5new.domain.users.ProviderUser;
import com.example.demo5new.domain.users.social.GoogleUser;

public final class OAuth2GoogleProviderUserConverter implements ProviderUserConverter<ProviderUserRequest,ProviderUser> {
    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {

        if (!providerUserRequest.clientRegistration().getRegistrationId().equals(OAuth2Config.SocialType.GOOGLE.getSocialName())) {
            return null;
        }

        return new GoogleUser(OAuth2Utils.getMainAttributes(
                providerUserRequest.oAuth2User()),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration());
    }
}
