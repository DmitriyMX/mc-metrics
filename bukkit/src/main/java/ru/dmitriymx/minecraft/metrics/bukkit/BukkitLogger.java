package ru.dmitriymx.minecraft.metrics.bukkit;

import lombok.RequiredArgsConstructor;
import ru.dmitriymx.minecraft.logger.LoggerAdapter;

import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class BukkitLogger extends LoggerAdapter {

    private final Logger originallLogger;

    @Override
    public void debug(String message) {
        originallLogger.log(Level.CONFIG, message);
    }

    @Override
    public void debug(String message, Throwable throwable) {
        originallLogger.log(Level.CONFIG, message, throwable);
    }

    @Override
    public void info(String message) {
        originallLogger.log(Level.INFO, message);
    }

    @Override
    public void warn(String message) {
        originallLogger.log(Level.WARNING, message);
    }

    @Override
    public void error(String message) {
        originallLogger.log(Level.SEVERE, message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        originallLogger.log(Level.SEVERE, message, throwable);
    }
}
