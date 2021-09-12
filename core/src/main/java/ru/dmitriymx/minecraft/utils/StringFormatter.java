package ru.dmitriymx.minecraft.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Copy-Paste from org.slf4j.helpers.MessageFormatter
 */
public final class StringFormatter {
    private static final String EMPTY = "";
    private static final char DELIM_START = '{';
    private static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    public static String arrayFormat(String messagePattern, Object[] argArray) {
        if (messagePattern == null) {
            return EMPTY;
        } else if (argArray == null) {
            return messagePattern;
        }

        StringBuilder sb = new StringBuilder(messagePattern.length() + 50);

        int k = 0;
        for (int i = 0; i < argArray.length; i++) {
            int idx = messagePattern.indexOf(DELIM_STR, k);

            if (idx == -1) {
                // no more variables
                if (k == 0) { // this is a simple string
                    return messagePattern;
                } else { // add the tail string which contains no variables and return
                         // the result.
                    sb.append(messagePattern, k, messagePattern.length());
                    return sb.toString();
                }
            } else {
                if (isEscapedDelimeter(messagePattern, idx)) {
                    if (!isDoubleEscaped(messagePattern, idx)) {
                        i--; // DELIM_START was escaped, thus should not be incremented
                        sb.append(messagePattern, k, idx - 1);
                        sb.append(DELIM_START);
                        k = idx + 1;
                    } else {
                        // The escape character preceding the delimiter start is
                        // itself escaped: "abc x:\\{}"
                        // we have to consume one backward slash
                        sb.append(messagePattern, k, idx - 1);
                        deeplyAppendParameter(sb, argArray[i], new HashMap<>());
                        k = idx + 2;
                    }
                } else {
                    sb.append(messagePattern, k, idx);
                    deeplyAppendParameter(sb, argArray[i], new HashMap<>());
                    k = idx + 2;
                }
            }
        }
        // append the characters following the last {} pair.
        sb.append(messagePattern, k, messagePattern.length());
        return sb.toString();
    }

    private static boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {
        if (delimeterStartIndex == 0) {
            return false;
        }
        char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
        return potentialEscape == ESCAPE_CHAR;
    }

    private static boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
        return delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == ESCAPE_CHAR;
    }

    // special treatment of array values was suggested by 'lizongbo'
    private static void deeplyAppendParameter(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap) {
        if (o == null) {
            sbuf.append("null");
            return;
        }
        if (!o.getClass().isArray()) {
            safeObjectAppend(sbuf, o);
        } else {
            // check for primitive array types because they
            // unfortunately cannot be cast to Object[]
            if (o instanceof boolean[]) {
                booleanArrayAppend(sbuf, (boolean[]) o);
            } else if (o instanceof byte[]) {
                byteArrayAppend(sbuf, (byte[]) o);
            } else if (o instanceof char[]) {
                charArrayAppend(sbuf, (char[]) o);
            } else if (o instanceof short[]) {
                shortArrayAppend(sbuf, (short[]) o);
            } else if (o instanceof int[]) {
                intArrayAppend(sbuf, (int[]) o);
            } else if (o instanceof long[]) {
                longArrayAppend(sbuf, (long[]) o);
            } else if (o instanceof float[]) {
                floatArrayAppend(sbuf, (float[]) o);
            } else if (o instanceof double[]) {
                doubleArrayAppend(sbuf, (double[]) o);
            } else {
                objectArrayAppend(sbuf, (Object[]) o, seenMap);
            }
        }
    }

    private static void safeObjectAppend(StringBuilder sbuf, Object o) {
        try {
            String oAsString = o.toString();
            sbuf.append(oAsString);
        } catch (Throwable t) {
            throw new RuntimeException("Failed toString() invocation on an object of type [" + o.getClass().getName() + "]", t);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private static void booleanArrayAppend(StringBuilder sbuf, boolean[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    @SuppressWarnings("DuplicatedCode")
    private static void byteArrayAppend(StringBuilder sbuf, byte[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    @SuppressWarnings("DuplicatedCode")
    private static void charArrayAppend(StringBuilder sbuf, char[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    @SuppressWarnings("DuplicatedCode")
    private static void shortArrayAppend(StringBuilder sbuf, short[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    @SuppressWarnings("DuplicatedCode")
    private static void intArrayAppend(StringBuilder sbuf, int[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    @SuppressWarnings("DuplicatedCode")
    private static void longArrayAppend(StringBuilder sbuf, long[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    @SuppressWarnings("DuplicatedCode")
    private static void floatArrayAppend(StringBuilder sbuf, float[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    @SuppressWarnings("DuplicatedCode")
    private static void doubleArrayAppend(StringBuilder sbuf, double[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void objectArrayAppend(StringBuilder sbuf, Object[] a, Map<Object[], Object> seenMap) {
        sbuf.append('[');
        if (!seenMap.containsKey(a)) {
            seenMap.put(a, null);
            int len = a.length;
            for (int i = 0; i < len; i++) {
                deeplyAppendParameter(sbuf, a[i], seenMap);
                if (i != len - 1) {
                    sbuf.append(", ");
                }
            }
            // allow repeats in siblings
            seenMap.remove(a);
        } else {
            sbuf.append("...");
        }
        sbuf.append(']');
    }
}
