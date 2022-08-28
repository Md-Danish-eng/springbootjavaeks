package com.springboot.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class WelcomeController {
 @GetMapping("/welcome")
	public String welcome() {
	 return "Hurray!!! I successfully deploy java app on eks cluster using jenkins ci/cd pipeline";
 }
}
