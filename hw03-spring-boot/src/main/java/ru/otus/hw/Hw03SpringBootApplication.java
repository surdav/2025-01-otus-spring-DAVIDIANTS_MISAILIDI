package ru.otus.hw;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import ru.otus.hw.service.TestRunnerService;

@SpringBootApplication
public class Hw03SpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(Hw03SpringBootApplication.class, args);
	}

	@Bean
	@Profile("default") // This bean will only be active when the "default" profile is active
	public CommandLineRunner commandLineRunner(TestRunnerService testRunnerService) {
		return args -> testRunnerService.run();
	}

}
