package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalizedIOServiceImpl implements LocalizedIOService {
    private final IOService ioService;

    private final LocalizationService localizationService;

    @Override
    public void printLineLocalized(String message, Object... args) {
        // Get the localized message and print it using IOService
        ioService.printLine(localizationService.getMessage(message, args));
    }
}
