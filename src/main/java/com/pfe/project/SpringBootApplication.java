package com.pfe.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@org.springframework.boot.autoconfigure.SpringBootApplication
//@SpringBootApplication
public class SpringBootApplication  {
	/*@Override  extends SpringBootServletInitializer
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringBootApplication.class);
	}*/
	public static void main(String[] args) {
		SpringApplication.run(SpringBootApplication.class, args);
	}

}
