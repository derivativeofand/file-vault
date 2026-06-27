package com.jha58.file_vault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class FileVaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileVaultApplication.class, args);
	}

}
