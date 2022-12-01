package com.example.demo5new.controller.admin;

import com.example.demo5new.controller.form.ResourcesCreateForm;
import com.example.demo5new.controller.form.ResourcesForm;
import com.example.demo5new.domain.Resources;
import com.example.demo5new.domain.Role;
import com.example.demo5new.repository.RoleRepository;
import com.example.demo5new.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import com.example.demo5new.service.ResourcesService;
import com.example.demo5new.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ResourcesController {

    private final ModelMapper modelMapper;
    private final UrlFilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;
    private final RoleRepository roleRepository;
    private final ResourcesService resourcesService;
    private final RoleService roleService;


    @GetMapping("/admin/resources")
    public String resourcesList(Model model) throws Exception {

        List<Resources> resources = resourcesService.getResources();
        model.addAttribute("resources", resources);

        return "admin/resource/list";
    }

    @GetMapping("/admin/resources/{id}")
    public String getResources(@PathVariable String id, Model model) {

        List<Role> roleList = roleService.getRoles();
        model.addAttribute("roleList", roleList);

        Resources resources = resourcesService.getResources(Long.valueOf(id));
        ResourcesForm resourcesForm = modelMapper.map(resources, ResourcesForm.class);
        model.addAttribute("resources", resourcesForm);

        return "admin/resource/detail";
    }

    @GetMapping(value="/admin/resources/register")
    public String viewRoles(Model model) throws Exception {

        List<Role> roleList = roleService.getRoles();
        model.addAttribute("roleList", roleList);

        ResourcesForm resourcesForm = new ResourcesForm();
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role());
        resourcesForm.setRoleSet(roleSet);
        model.addAttribute("resources", resourcesForm);

        return "admin/resource/detail";
    }
    
    @PostMapping("/admin/resources")
    public String createResources(ResourcesCreateForm resourcesCreateForm) {

        ModelMapper modelMapper = new ModelMapper();
        Role role = roleRepository.findByRoleName(resourcesCreateForm.getRoleName());
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        Resources resources = modelMapper.map(resourcesCreateForm, Resources.class);
        resources.setRoleSet(roles);

        resourcesService.createResources(resources);
        filterInvocationSecurityMetadataSource.reload();

        return "redirect:/admin/resources";
    }

    @GetMapping("/admin/resources/delete/{id}") // todo; to use DELETE method
    public String removeResources(@PathVariable String id, Model model) {

        resourcesService.deleteResources(Long.valueOf(id));
        filterInvocationSecurityMetadataSource.reload();

        return "redirect:/admin/resources";
    }

}
