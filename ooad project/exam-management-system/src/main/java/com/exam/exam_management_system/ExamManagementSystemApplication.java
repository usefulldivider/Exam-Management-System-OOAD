package com.exam.exam_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ExamManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamManagementSystemApplication.class, args);
	}

}
