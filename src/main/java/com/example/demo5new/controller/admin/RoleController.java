package com.example.demo5new.controller.admin;

import com.example.demo5new.controller.form.RoleForm;
import com.example.demo5new.domain.Role;
import com.example.demo5new.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoleController {

	private final RoleService roleService;

	@GetMapping(value="/admin/roles")
	public String getRoles(Model model) throws Exception {

		List<Role> roles = roleService.getRoles();
		model.addAttribute("roles", roles);

		return "admin/role/list";
	}

	@GetMapping(value="/admin/roles/register")
	public String viewRoles(Model model) throws Exception {

		RoleForm role = new RoleForm();
		model.addAttribute("role", role);

		return "admin/role/detail";
	}

	@PostMapping(value="/admin/roles")
	public String createRole(RoleForm roleForm) throws Exception {

		ModelMapper modelMapper = new ModelMapper();
		Role role = modelMapper.map(roleForm, Role.class);
		roleService.createRole(role);

		return "redirect:/admin/roles";
	}

	@GetMapping(value="/admin/roles/{id}")
	public String getRole(@PathVariable String id, Model model) throws Exception {

		Role role = roleService.getRole(Long.valueOf(id));

		ModelMapper modelMapper = new ModelMapper();
		RoleForm roleForm = modelMapper.map(role, RoleForm.class);
		model.addAttribute("role", roleForm);

		return "admin/role/detail";
	}

	@GetMapping(value="/admin/roles/delete/{id}")
	public String deleteResources(@PathVariable String id, Model model) throws Exception {

		//Role role = roleService.getRole(Long.valueOf(id));
		roleService.deleteRole(Long.valueOf(id));

		return "redirect:/admin/resources";
	}
}
