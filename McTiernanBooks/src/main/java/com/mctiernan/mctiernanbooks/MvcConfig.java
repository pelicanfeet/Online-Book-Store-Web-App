package com.mctiernan.mctiernanbooks;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
//		registry.addViewController("/").setViewName("home");
//		registry.addViewController("/home").setViewName("home");
//		registry.addViewController("/book/detail").setViewName("detail");
//		registry.addViewController("/book/add").setViewName("add");
//		registry.addViewController("/book/delete").setViewName("delete");
//		registry.addViewController("/login").setViewName("login");
//
// 		Note: These do the same thing as specifying a mapping in the Controller
//			  via the @RequestMapping(...) tag.	
	}

}