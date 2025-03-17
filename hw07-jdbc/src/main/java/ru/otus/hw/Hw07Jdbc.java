package ru.otus.hw;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import ru.otus.hw.services.TestRunnerService;

@SpringBootApplication
public class Hw07Jdbc {

	public static void main(String[] args) {
		SpringApplication.run(Hw07Jdbc.class, args);
	}

	@Bean
	@Profile("!test") // The bean will not be registered in the 'test' profile
	@ConditionalOnExpression("'${spring.shell.interactive.enabled}' == 'false'")
	public CommandLineRunner libraryCommandLineRunner(TestRunnerService testRunnerService) {
		return args -> testRunnerService.run();
	}

}
