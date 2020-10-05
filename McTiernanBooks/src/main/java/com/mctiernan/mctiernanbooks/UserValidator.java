package com.mctiernan.mctiernanbooks;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object target, Errors errors) {
		User user = (User)target;
		
		String password = user.getPassword();
		String confPassword = user.getConfPassword();
		
		if(!password.equals(confPassword)) {
			errors.rejectValue("password", "user.password.misMatch");
			errors.rejectValue("confPassword", "user.password.misMatch");
		}
	}

}