package com.example.consultorio;

import org.springframework.boot.SpringApplication;

public class TestConsultorioApplication {

	public static void main(String[] args) {
		SpringApplication.from(ConsultorioApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
