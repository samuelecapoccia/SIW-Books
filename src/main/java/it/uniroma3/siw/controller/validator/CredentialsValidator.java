package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.service.CredentialsService;

@Component
public class CredentialsValidator {
	
	@Autowired
	private CredentialsService credentialsService;
	
	public void validate(Object o, Errors errors) {
		Credentials credentials = (Credentials) o;
		if(credentials.getEmail()!=null && credentialsService.emailExists(credentials.getEmail())) {
			errors.reject("credentials.duplicate");
		}
	}
	
	public boolean supports(Class<?> aClass) {
		return Credentials.class.equals(aClass);
	}
	
	
}
