package ru.dmitriymx.minecraft.logger;

import ru.dmitriymx.minecraft.utils.StringFormatter;

public abstract class LoggerAdapter {

    public abstract void debug(String message);
    public abstract void debug(String message, Throwable throwable);
    public abstract void info(String message);
    public abstract void warn(String message);
    public abstract void error(String message);
    public abstract void error(String message, Throwable throwable);

    public void debug(String pattern, Object... objects) {
        FormattingPair formattingPair = LoggerFormatter.arrayFormat(pattern, objects);
        if (formattingPair.getThrowable() != null) {
            debug(formattingPair.getMessage(), formattingPair.getThrowable());
        } else {
            debug(formattingPair.getMessage());
        }
    }

    public void info(String pattern, Object... objects) {
        info(StringFormatter.arrayFormat(pattern, objects));
    }

    public void warn(String pattern, Object... objects) {
        warn(StringFormatter.arrayFormat(pattern, objects));
    }

    public void error(String pattern, Object... objects) {
        FormattingPair formattingPair = LoggerFormatter.arrayFormat(pattern, objects);
        if (formattingPair.getThrowable() != null) {
            error(formattingPair.getMessage(), formattingPair.getThrowable());
        } else {
            error(formattingPair.getMessage());
        }
    }
}
