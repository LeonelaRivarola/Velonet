package com.velonet.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.velonet.backend.dto.TestDto;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);

		TestDto dto = new TestDto();
		dto.setNombre("Juan");

		System.out.println(dto.getNombre());
	}

}
