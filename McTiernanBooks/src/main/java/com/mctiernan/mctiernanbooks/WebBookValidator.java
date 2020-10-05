package com.mctiernan.mctiernanbooks;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class WebBookValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return WebBook.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object target, Errors errors) {
		WebBook webBook = (WebBook)target;
		
		String isbn = webBook.getIsbn();
		String isbnConfirm = webBook.getIsbnConfirm();
		
		if(!isbn.equals(isbnConfirm)) {
			errors.rejectValue("isbn", "webBook.isbn.misMatch");
			errors.rejectValue("isbnConfirm", "webBook.isbn.misMatch");
		}
	}
}