package com.example.cursobatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"com.example.controller", "com.example.configuration"})
public class CursobatchApplication {
	
		
	public static void main(String[] args) {
		SpringApplication.run(CursobatchApplication.class, args);
		System.out.println("This is a Test");
	}

}
