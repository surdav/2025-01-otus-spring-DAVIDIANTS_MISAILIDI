package ru.otus.hw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import jakarta.annotation.PostConstruct;
import java.util.Locale;

@Configuration
public class LocalizationConfig {

    @Value("${defaultLocale:es}") // Load default locale from application.yml
    private String defaultLocale;

    @PostConstruct
    public void configureDefaultLocale() {
        // Set the global JVM-wide locale based on the configuration
        Locale.setDefault(new Locale(defaultLocale));
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        // Configure the message source for loading localized resources
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages"); // Look for messages.properties and other locale versions
        messageSource.setDefaultEncoding("UTF-8"); // Ensure proper encoding for files
        messageSource.setUseCodeAsDefaultMessage(true); // Return the code if no message is found
        return messageSource;
    }
}