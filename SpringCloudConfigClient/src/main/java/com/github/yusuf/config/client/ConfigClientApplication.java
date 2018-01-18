package com.github.yusuf.config.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ConfigClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigClientApplication.class, args);
	}
}

@RefreshScope
@RestController
class UserRestController {

	@Value("${user.name}")
	private String name;

	@Value("${user.city}")
	private String city;

	@Value("${user.language}")
	private String language;

	@Value("${user.state}")
	private String state;

	@RequestMapping("/getUserDetails")
	String getUserDetails() {
		return "Name :" + name + "<p>City :" + city + "<p>State :" + state + "<p>Language :" + language;
	}
}