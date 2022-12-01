package com.example.demo5new.security.listener;

import com.example.demo5new.domain.AccessIp;
import com.example.demo5new.domain.Resources;
import com.example.demo5new.domain.Role;
import com.example.demo5new.domain.RoleHierarchy;
import com.example.demo5new.domain.user.Account;
import com.example.demo5new.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Transactional
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;
    private final String IP_ADDRESS = "14.6.230.128";
    private final String LOCALHOST = "0:0:0:0:0:0:0:1";
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final ResourcesRepository resourcesRepository;
    private final AccessIpRepository accessIpRepository;
    private final RoleHierarchyRepository roleHierarchyRepository;

    private static AtomicInteger count = new AtomicInteger(0);


    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        setupSecurityResources();
        setupAccessIpData();
        alreadySetup = true;
    }

    private void setupAccessIpData() {
        if(!accessIpRepository.existsByIpAddress(IP_ADDRESS)) {
            AccessIp accessIp = AccessIp.builder()
                    .ipAddress(IP_ADDRESS)
                    .build();
            accessIpRepository.save(accessIp);
        }
    }

    private void setupSecurityResources() {

        Set<Role> roles = new HashSet<>();
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
        roles.add(adminRole);
        createResourceIfNotFound("/admin/**", "", roles, "url");
        createResourceIfNotFound("/config", "", roles, "url");
        createUserIfNotFound("admin", "pass", "admin@gmail.com", roles);

        Set<Role> roles2 = new HashSet<>();
        Role managerRole = createRoleIfNotFound("ROLE_MANAGER");
        roles2.add(managerRole);
        createResourceIfNotFound("/manager/**", "", roles2, "url");
        createResourceIfNotFound("/messages", "", roles2, "url");
        createUserIfNotFound("manager", "pass", "manager@gmail.com", roles2);

        Set<Role> roles3 = new HashSet<>();
        Role userRole = createRoleIfNotFound("ROLE_USER");
        roles3.add(userRole);
        createResourceIfNotFound("/user/**", "", roles3, "url");
        createResourceIfNotFound("/mypage", "", roles3, "url");
        createUserIfNotFound("user", "pass", "user@gmail.com", roles3);

        createRoleHierarchyIfNotFound(managerRole, adminRole);
        createRoleHierarchyIfNotFound(userRole, managerRole);
    }

    public Role createRoleIfNotFound(String roleName) {

        Role role = roleRepository.findByRoleName(roleName);

        if (role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .build();
        }
        return roleRepository.save(role);
    }

    public Account createUserIfNotFound(String userName, String password, String email, Set<Role> roleSet) {

        Account account = accountRepository.findByUsername(userName);

        if (account == null) {
            account = Account.builder()
                    .username(userName)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .userRoles(roleSet)
                    .build();
        }
        return accountRepository.save(account);
    }

    public Resources createResourceIfNotFound(String resourceName, String httpMethod, Set<Role> roleSet, String resourceType) {
        Resources resources = resourcesRepository.findByResourceNameAndHttpMethod(resourceName, httpMethod);

        if (resources == null) {
            resources = Resources.builder()
                    .resourceName(resourceName)
                    .roleSet(roleSet)
                    .httpMethod(httpMethod)
                    .resourceType(resourceType)
                    .orderNum(count.incrementAndGet())
                    .build();
        }
        return resourcesRepository.save(resources);
    }

    public void createRoleHierarchyIfNotFound(Role childRole, Role parentRole) {

        RoleHierarchy roleHierarchy = roleHierarchyRepository.findByChildName(parentRole.getRoleName());
        if (roleHierarchy == null) {
            roleHierarchy = RoleHierarchy.builder()
                    .childName(parentRole.getRoleName())
                    .build();
        }
        RoleHierarchy parentRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);

        roleHierarchy = roleHierarchyRepository.findByChildName(childRole.getRoleName());
        if (roleHierarchy == null) {
            roleHierarchy = RoleHierarchy.builder()
                    .childName(childRole.getRoleName())
                    .build();
        }

        RoleHierarchy childRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);
        childRoleHierarchy.setParentName(parentRoleHierarchy);
    }
}