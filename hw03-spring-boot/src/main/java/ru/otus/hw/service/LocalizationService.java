package ru.otus.hw.service;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocalizationService {

    private final ResourceBundleMessageSource messageSource;

    public LocalizationService(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // Retrieve a message by its key for the current locale
    public String getMessage(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }

    // Retrieve a message by its key with arguments for the current locale
    public String getMessage(String key, Object[] args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }
}