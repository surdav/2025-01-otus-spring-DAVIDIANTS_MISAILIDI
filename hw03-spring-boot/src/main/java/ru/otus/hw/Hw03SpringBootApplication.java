package ru.otus.hw;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.otus.hw.service.TestRunnerService;

@SpringBootApplication
public class Hw03SpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(Hw03SpringBootApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(TestRunnerService testRunnerService) {
		return args -> testRunnerService.run();
	}

}
