package ru.dmitriymx.minecraft.metrics.sponge;

import lombok.RequiredArgsConstructor;
import ru.dmitriymx.minecraft.logger.LoggerAdapter;

@RequiredArgsConstructor
public class SpongeLogger extends LoggerAdapter {

    private final org.slf4j.Logger logger;

    @Override
    public void debug(String message) {
        logger.debug(message);
    }

    @Override
    public void debug(String message, Throwable throwable) {
        logger.debug(message, throwable);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}
