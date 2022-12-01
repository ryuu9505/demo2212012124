package com.example.demo5new.controller;

import com.example.demo5new.domain.user.Account;
import com.example.demo5new.domain.users.PrincipalUser;
import com.example.demo5new.domain.users.ProviderUser;
import com.example.demo5new.security.common.CurrentAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.example.demo5new.security.common.SecurityUrl.ACCESS_DENIED_URL;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal PrincipalUser principalUser) {

        String view = "index";

        if (principalUser != null) {

            ProviderUser providerUser = principalUser.providerUser();
            log.info("id={}", providerUser.getId());
            log.info("username={}", providerUser.getUsername());
            log.info("provider={}", providerUser.getProvider());
            log.info("email={}", providerUser.getEmail());
            log.info("picture={}", providerUser.getPicture());
            log.info("authorities={}", providerUser.getAuthorities().toString());
            log.info("attributes={}", providerUser.getAttributes());

            model.addAttribute("user", providerUser.getUsername());
            model.addAttribute("provider", providerUser.getProvider());
            if(!principalUser.providerUser().isCertificated()) view = "selfcert";
        }

        return view;
    }

    @GetMapping("/api/user")
    public Authentication user(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2User) {
        System.out.println("authentication = " + authentication + ", oAuth2User = " + oAuth2User);
        return authentication;
    }

    @GetMapping("/api/oidc") // 요청시 scope 에 openid 가 포함되어야 oidcUser 가 생성된다
    public Authentication oidc(Authentication authentication, @AuthenticationPrincipal OidcUser oidcUser) {
        System.out.println("authentication = " + authentication + ", oidcUser = " + oidcUser);
        return authentication;
    }

    @GetMapping(ACCESS_DENIED_URL)
    public String denied(
            @CurrentAccount ProviderUser account, // todo
            @RequestParam(value = "exception", required = false) String exception,
            Model model) {
        model.addAttribute("username", account.getUsername());
        model.addAttribute("exception", exception);
        return "denied";
    }

    /**
     * todo delete
     * TEST methods
     */

    @GetMapping("/user/test")
    public String testUser() {
        return "test/user";
    }

    @GetMapping("/manager/test")
    public String testManager() {
        return "test/manager";
    }

    @GetMapping("/admin/test")
    public String testAdmin() {
        return "test/admin";
    }

    @GetMapping("/test")
    public String test() {
        return "test/test";
    }
}
