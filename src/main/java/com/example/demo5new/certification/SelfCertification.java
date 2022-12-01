package com.example.demo5new.certification;

import com.example.demo5new.domain.users.ProviderUser;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class SelfCertification {

//    private final UserRepository userRepository;

    public void checkCertification(ProviderUser providerUser) {

        //User user = userRepository.findByUsername(providerUser.getId());
//        if(user != null) {
        boolean bool = providerUser.getProvider().equals("none") || providerUser.getProvider().equals("naver");
        providerUser.isCertificated(bool);
//        }
    }

    public void certificate(ProviderUser providerUser) {

    }
}
