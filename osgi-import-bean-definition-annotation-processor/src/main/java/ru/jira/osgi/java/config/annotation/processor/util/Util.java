package ru.jira.osgi.java.config.annotation.processor.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class Util {

    private Util() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static String getStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
