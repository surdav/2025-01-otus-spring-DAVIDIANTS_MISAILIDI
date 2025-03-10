package ru.otus.hw;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.service.TestRunnerService;

@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class Hw05AdvConf {

	public static void main(String[] args) {
		SpringApplication.run(Hw05AdvConf.class, args);
	}

	@Bean
	@ConditionalOnProperty(name = "spring.shell.interactive.enabled", havingValue = "false", matchIfMissing = true)
	public CommandLineRunner advancedCommandLineRunner(TestRunnerService testRunnerService) {
		return args -> testRunnerService.run();
	}

}
