package ru.jira.osgi.java.config.annotation.processor.util;

public final class Const {

    private Const() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static final String APPLICATION_PROPERTIES_CLASS_FROM_JIRA_CONFIG = "com.atlassian.jira.config.properties.ApplicationProperties";
    public static final String APPLICATION_PROPERTIES_CLASS_FROM_SAL_API = "com.atlassian.sal.api.ApplicationProperties";

    public static final char DOT = '.';
}
