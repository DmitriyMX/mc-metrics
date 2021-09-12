package ru.dmitriymx.minecraft.logger;

import ru.dmitriymx.minecraft.utils.StringFormatter;

/**
 * Copy-Paste from org.slf4j.helpers.MessageFormatter
 */
public final class LoggerFormatter {

    public static FormattingPair arrayFormat(String messagePattern, Object[] argArray) {
        Object[] args;
        Throwable throwableCandidate = getThrowableCandidate(argArray);
        if (throwableCandidate != null) {
            args = trimmedCopy(argArray);
        } else {
            args = argArray;
        }

        return arrayFormat(messagePattern, args, throwableCandidate);
    }

    public static FormattingPair arrayFormat(String messagePattern, Object[] argArray, Throwable throwable) {
        if (messagePattern == null) {
            return new FormattingPair(null, throwable);
        }

        if (argArray == null) {
            return new FormattingPair(messagePattern);
        }

        return new FormattingPair(StringFormatter.arrayFormat(messagePattern, argArray), throwable);
    }

    private static Throwable getThrowableCandidate(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return null;
        }

        Object lastEntry = argArray[argArray.length - 1];
        if (lastEntry instanceof Throwable) {
            return (Throwable) lastEntry;
        }

        return null;
    }

    private static Object[] trimmedCopy(final Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            throw new IllegalStateException("non-sensical empty or null argument array");
        }

        int trimmedLen = argArray.length - 1;
        Object[] trimmed = new Object[trimmedLen];

        if (trimmedLen > 0) {
            System.arraycopy(argArray, 0, trimmed, 0, trimmedLen);
        }

        return trimmed;
    }
}
