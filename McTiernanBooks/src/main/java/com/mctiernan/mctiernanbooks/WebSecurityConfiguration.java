package com.mctiernan.mctiernanbooks;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
			.passwordEncoder(passwordEncoder)
			.usersByUsernameQuery("SELECT email, password, active FROM account "
								  + " where email=?")
			.authoritiesByUsernameQuery("select email, role from authority "
								  + " where email=?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/login").permitAll()
				.antMatchers("/logout").permitAll()
				.antMatchers("/", "/register", "/home").permitAll()
				.antMatchers("/book/all").permitAll()
				.antMatchers("/book/detail/**").hasAnyRole("CUSTOMER", "EMP", "ADMIN")
				.antMatchers("/book/searchForm/**").hasAnyRole("CUSTOMER", "EMP", "ADMIN")
				.antMatchers("/book/add/**").hasAnyRole("EMP", "ADMIN")
				.antMatchers("/book/delete/**").hasRole("ADMIN")
				.antMatchers("/book/deleteForm/**").hasRole("ADMIN")
				.antMatchers("/user/all").hasAnyRole("EMP", "ADMIN")
				.anyRequest().authenticated()
			.and()
				.formLogin()
					.loginPage("/login")
					.failureUrl("/login?error")
					.defaultSuccessUrl("/home")
			.and()
				.exceptionHandling()
					.accessDeniedPage("/403");
	}

}