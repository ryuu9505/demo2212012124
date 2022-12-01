package com.example.demo5new.common.authority;

import com.example.demo5new.domain.Role;
import com.example.demo5new.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final RoleRepository roleRepository;
    private final String PREFIX_ROLE = "ROLE_";
    private final String PREFIX_SCOPE = "SCOPE_";

    public Set<Role> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        HashSet<Role> mapped = new HashSet<>(authorities.size());
        for (GrantedAuthority authority : authorities) {
            mapped.add(mapAuthority(authority.getAuthority()));
        }
        return mapped;
    }

    public List<GrantedAuthority> mapRoles(Set<Role> roles) {
        List<GrantedAuthority> mapped = new ArrayList<>(roles.size());
        for (Role role : roles) {
            mapped.add(mapRole(role.getRoleName()));
        }
        return mapped;
    }

    private Role mapAuthority(String name) {
        if(name.lastIndexOf(".") > 0){
            int index = name.lastIndexOf(".");
            name = PREFIX_SCOPE + name.substring(index+1);
        } else if (!name.startsWith(PREFIX_ROLE) || !name.startsWith(PREFIX_ROLE)) {
            name = PREFIX_ROLE + name;
        }
        return createRoleIfNotFound(name);
    }

    private GrantedAuthority mapRole(String name) {
        return new SimpleGrantedAuthority(name);
    }

    private Role createRoleIfNotFound(String roleName) {

        Role role = roleRepository.findByRoleName(roleName);

        if (role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .build();
        }
        return roleRepository.save(role);
    }
}
