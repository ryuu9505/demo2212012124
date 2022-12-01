package com.example.demo5new.service.impl;

import com.example.demo5new.domain.RoleHierarchy;
import com.example.demo5new.repository.RoleHierarchyRepository;
import com.example.demo5new.service.RoleHierarchyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Service("roleHierarchyService")
@RequiredArgsConstructor
public class RoleHierarchyServiceImpl implements RoleHierarchyService {

    private final RoleHierarchyRepository roleHierarchyRepository;

    @Transactional
    @Override
    public String findAllHierarchy() {

        List<RoleHierarchy> rolesHierarchy = roleHierarchyRepository.findAll();

        Iterator<RoleHierarchy> itr = rolesHierarchy.iterator();
        StringBuffer concatenatedRoles = new StringBuffer();
        while (itr.hasNext()) {
            RoleHierarchy model = itr.next();
            if (model.getParentName() != null) {
                concatenatedRoles.append(model.getParentName().getChildName());
                concatenatedRoles.append(" > ");
                concatenatedRoles.append(model.getChildName());
                concatenatedRoles.append("\n");
            }
        }
        return concatenatedRoles.toString();

    }
}