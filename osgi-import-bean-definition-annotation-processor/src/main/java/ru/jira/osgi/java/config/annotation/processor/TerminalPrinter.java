package ru.jira.osgi.java.config.annotation.processor;

import ru.jira.osgi.java.config.annotation.processor.util.Util;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import java.util.Objects;

public class TerminalPrinter {
    private final ProcessingEnvironment processingEnvironment;

    public TerminalPrinter(final ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = Objects.requireNonNull(processingEnvironment, "processingEnvironment");
    }

    public void printNote(final String message) {
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
    }

    public void printError(final String message, Throwable throwable) {
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, message + " ---- " + Util.getStackTrace(throwable));
    }

    public void printError(Throwable throwable) {
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, Util.getStackTrace(throwable));
    }
}
