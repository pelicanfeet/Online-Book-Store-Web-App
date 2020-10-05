package com.mctiernan.mctiernanbooks;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class User {

	@NotNull
	@NotEmpty
	@Email
	private String email;
	
	@NotNull
	@NotEmpty
	private String firstName;
	
	@NotNull
	@NotEmpty
	private String lastName;
	
	@NotNull
	@NotEmpty
	private String homeAddress;
	
	@NotNull
	@NotEmpty
	@Size(min=3,max=15)
	private String password;
	
	@NotNull
	@NotEmpty
	@Size(min=3,max=15)
	private String confPassword;
	
	@NotNull
	@NotEmpty(message="Must select at least one role")
	private List<String> roles = new ArrayList<>();

	public User() {}
	
	public User(String email, String firstName, String lastName, String homeAddress,
				String password, String confPassword, List<String> roles) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.homeAddress = homeAddress;
		this.password = password;
		this.confPassword = confPassword;
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getHomeAddress() {
		return homeAddress;
	}


	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	public void addRole(String role) {
		this.roles.add(role);
	}

	public String getConfPassword() {
		return confPassword;
	}

	public void setConfPassword(String confPassword) {
		this.confPassword = confPassword;
	}

}