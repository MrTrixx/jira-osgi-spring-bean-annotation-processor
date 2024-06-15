package ru.jira.osgi.java.config.annotation.processor;

import ru.jira.osgi.java.config.annotation.processor.dto.BeanDefinitionData;
import ru.jira.osgi.java.config.annotation.processor.dto.ConfigurationClazzData;
import ru.jira.osgi.java.config.annotation.processor.util.Const;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpringConfigurationImportOSGIBeanGenerator {
    private static final String EMPTY_CONFIGURATION_CLASS_MSG_TEMPLATE = "ConfigurationClass %s for generating no has bean definitions or has duplicates from other ConfigurationClasses";
    private static final String GENERATED_CLASSES_MSG_TEMPLATE = "%s were generated configurations classes %s";
    private static final String SPRING_CONFIGURATION_ANNOTATION_SIMPLE_NAME = "Configuration";
    private static final String SPRING_CONFIGURATION_ANNOTATION_CLASS = "org.springframework.context.annotation." + SPRING_CONFIGURATION_ANNOTATION_SIMPLE_NAME;
    private static final String SPRING_BEAN_ANNOTATION_SIMPLE_NAME = "Bean";
    private static final String SPRING_BEAN_ANNOTATION_CLASS = "org.springframework.context.annotation." + SPRING_BEAN_ANNOTATION_SIMPLE_NAME;
    private static final String JIRA_SPRING_JAVA_IMPORT_OSGI_SERVICE_CLASS = "com.atlassian.plugins.osgi.javaconfig.OsgiServices";


    private final TerminalPrinter terminalPrinter;
    private final List<String> generatedClassesName = new ArrayList<>();
    private int countGeneratedClasses = 0;

    public SpringConfigurationImportOSGIBeanGenerator(final TerminalPrinter terminalPrinter) {
        this.terminalPrinter = terminalPrinter;
    }

    public void generateConfigurationClassWithBeanDefinitions(
            final ConfigurationClazzData configurationClazzData,
            final ProcessingEnvironment processingEnv) throws IOException {
        List<BeanDefinitionData> beanDefinitionData = configurationClazzData.getBeanDefinitionData();

        if (beanDefinitionData.isEmpty()) {
            terminalPrinter.printNote(String.format(EMPTY_CONFIGURATION_CLASS_MSG_TEMPLATE, configurationClazzData.getSourceAnnotatedClassName()));
            return;
        }

        writeBuilderFile(configurationClazzData, processingEnv);
    }

    public void infoUserAboutGeneratedClasses() {
        if (countGeneratedClasses == 0) {
            terminalPrinter.printNote("Nothing generated for import osgi jira components");
        } else {
            terminalPrinter.printNote(String.format(GENERATED_CLASSES_MSG_TEMPLATE, countGeneratedClasses, generatedClassesName));
        }
    }

    private void writeBuilderFile(
            final ConfigurationClazzData configurationClazzData,
            final ProcessingEnvironment processingEnv) throws IOException {

        String className = configurationClazzData.getSourceAnnotatedClassName();
        String packageName = null;
        int lastDot = className.lastIndexOf(Const.DOT);
        if (lastDot > 0) {
            packageName = className.substring(0, lastDot);
        }

        Objects.requireNonNull(packageName, "packageName of generated configuration class name cannot be null");

        String fullClassNameToGenerate = className + "GeneratedOsgiImportConfiguration";
        String simpleClassNameToGenerate = fullClassNameToGenerate.substring(lastDot + 1);

        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(fullClassNameToGenerate);
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            generatePackageLine(packageName, out);
            generateSpringFrameworkAndOsgiServiceUtilClassImports(out);

            generateConfigurationClassWithBeanDefinitions(
                    out,
                    simpleClassNameToGenerate,
                    configurationClazzData.getBeanDefinitionData()
            );
        }

        countGeneratedClasses++;
        generatedClassesName.add(fullClassNameToGenerate);
    }

    private static void generatePackageLine(final String packageName, final PrintWriter out) {
        if (packageName != null) {
            out.print("package ");
            out.print(packageName);
            out.println(";");
            out.println();
        }
    }

    private static void generateSpringFrameworkAndOsgiServiceUtilClassImports(final PrintWriter out) {
        generateImport(out, SPRING_CONFIGURATION_ANNOTATION_CLASS);
        generateImport(out, SPRING_BEAN_ANNOTATION_CLASS);
        generateImport(out, JIRA_SPRING_JAVA_IMPORT_OSGI_SERVICE_CLASS);
        out.println();
    }

    private static void generateImport(final PrintWriter out, final String clazzNameToImport) {
        out.println("import " + clazzNameToImport + ";");
    }

    private static void generateConfigurationClassWithBeanDefinitions(
            final PrintWriter out,
            final String simpleClassNameToGenerate,
            final List<BeanDefinitionData> beanDefinitionsData) {
        out.println("@" + SPRING_CONFIGURATION_ANNOTATION_SIMPLE_NAME);
        out.print("public class ");
        out.print(simpleClassNameToGenerate);
        out.println(" {");
        out.println();

        generateBeanDefinitions(out, beanDefinitionsData);

        out.println("}");
    }

    private static void generateBeanDefinitions(final PrintWriter out, final List<BeanDefinitionData> beanDefinitionsData) {
        for (BeanDefinitionData beanDefinition : beanDefinitionsData) {
            String fullClazzName = beanDefinition.getFullClazzName();

            out.println("    @" + SPRING_BEAN_ANNOTATION_SIMPLE_NAME);
            out.print("    ");
            out.print(fullClazzName);
            out.println(String.format(" %s() {", beanDefinition.getBeanDefinitionName()));
            out.println(String.format("        return (%s) OsgiServices.importOsgiService(%s.class);", fullClazzName, fullClazzName));
            out.println("    }");
            out.println();
        }
    }
}