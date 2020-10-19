package com.mctiernan.mctiernanbooks;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserValidator userValidator;

	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String displayForm(User user, Model model) {
		logger.info("GET(/register");
		model.addAttribute("roleTypes", userDao.getRoleTypes());
		return "registerForm";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String processForm(@Valid User user, BindingResult bindingResult, Model model) {
		userValidator.validate(user, bindingResult);
		if(bindingResult.hasErrors()) {
			model.addAttribute("roleTypes", userDao.getRoleTypes());
			return "registerForm";
		}
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		model.addAttribute("user", user);
		
		user = userDao.createUser(user);
		List<String> roles = user.getRoles();
		for(int i = 0; i < roles.size(); i++) {
			roles.set(i, "ROLE_" + roles.get(i));
		}
		logger.info("User created: " + user.getEmail());
		
		autologin(user, roles);
		
		return "redirect:/home";
	}
	
	@RequestMapping(value = "/user/all", method = RequestMethod.GET)
	public String displayAllUsers(Model model) {
		model.addAttribute("users", userDao.getAllUsers());
		return "users";
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Principal principal, Model model) {
		User user = userDao.getUserByEmail(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("firstName", user.getFirstName());
		model.addAttribute("lastName", user.getLastName());

		return "home";
	}

	private void autologin(User user, List<String> roles) {
		Collection<GrantedAuthority> authorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(String.join(", ", roles));
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
				user.getEmail(), null, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

}