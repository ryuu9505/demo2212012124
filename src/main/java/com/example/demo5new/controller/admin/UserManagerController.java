package com.example.demo5new.controller.admin;

import com.example.demo5new.controller.form.AccountForm;
import com.example.demo5new.domain.Role;
import com.example.demo5new.domain.user.Account;
import com.example.demo5new.service.AccountService;
import com.example.demo5new.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserManagerController {
	
	private final AccountService accountService;
	private final RoleService roleService;

	@GetMapping("/admin/accounts")
	public String getAccounts(Model model) throws Exception {

		List<Account> accounts = accountService.getAccounts();
		model.addAttribute("accounts", accounts);

		return "admin/user/list";
	}

	@PostMapping("/admin/accounts")
	public String modifyAccount(AccountForm accountForm) throws Exception {

		accountService.modifyAccount(accountForm);

		return "redirect:/admin/accounts";
	}

	@GetMapping("/admin/accounts/{id}")
	public String getAccount(@PathVariable(value = "id") Long id, Model model) {

		AccountForm accountForm = accountService.getAccount(id);
		List<Role> roleList = roleService.getRoles();

		model.addAttribute("account", accountForm);
		model.addAttribute("roleList", roleList);

		return "admin/user/detail";
	}

	@GetMapping(value = "/admin/accounts/delete/{id}")
	public String deleteAccount(@PathVariable(value = "id") Long id, Model model) {

		accountService.deleteAccount(id);

		return "redirect:/admin/users";
	}
}
