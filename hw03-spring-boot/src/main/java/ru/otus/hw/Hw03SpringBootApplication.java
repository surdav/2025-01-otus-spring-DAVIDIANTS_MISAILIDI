package ru.otus.hw;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.service.TestRunnerService;

@EnableConfigurationProperties(AppProperties.class)
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
