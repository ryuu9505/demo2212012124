package com.example.demo5new.security.common;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // retention in runtime
@Target(ElementType.PARAMETER) // parameter only
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : providerUser")
public @interface CurrentAccount {
}
