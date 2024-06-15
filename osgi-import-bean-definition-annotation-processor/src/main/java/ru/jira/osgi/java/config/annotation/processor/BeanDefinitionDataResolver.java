package ru.jira.osgi.java.config.annotation.processor;

import ru.jira.osgi.java.config.annotation.processor.dto.BeanDefinitionData;
import ru.jira.osgi.java.config.annotation.processor.util.Const;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class BeanDefinitionDataResolver {
    private static final int INDEX_OF_FIRST_CHAR_IN_CLASS_NAME = 0;
    private static final int INDEX_OF_SECOND_CHAR_IN_CLASS_NAME_FOR_SUBSTRING = 1;
    private static final int START_INDEX_TO_SUBSTRING_CLASS_NAME_FOR_DEFINE_PACKAGE_NAME = 0;
    private static final String MESSAGE_TEMPLATE_ABOUT_ALREADY_GENERATED_BEAN_DEFINITION = "Bean definition of class %s which was generated";
    private static final String MESSAGE_TEMPLATE_VALIDATION_PACKAGE_NAME_BEAN_DEFINITION = "Class %s has invalid package name. It cannot be null";

    private final Set<BeanDefinitionData> alreadyGenerated;
    private final TerminalPrinter terminalPrinter;
    private final SameBeanDefinitionClassNameHolder sameBeanDefinitionClassNameHolder;

    public BeanDefinitionDataResolver(
            final TerminalPrinter terminalPrinter,
            final SameBeanDefinitionClassNameHolder sameBeanDefinitionClassNameHolder) {
        this.terminalPrinter = terminalPrinter;
        this.sameBeanDefinitionClassNameHolder = sameBeanDefinitionClassNameHolder;
        this.alreadyGenerated = new HashSet<>();
    }

    public Optional<BeanDefinitionData> processStringClassNameToBeanDefinitionData(final String sourceClassName, final String clazzNameForBeanDefinition) {
        String packageName = null;
        int lastDot = clazzNameForBeanDefinition.lastIndexOf(Const.DOT);
        if (lastDot > 0) {
            packageName = clazzNameForBeanDefinition.substring(START_INDEX_TO_SUBSTRING_CLASS_NAME_FOR_DEFINE_PACKAGE_NAME, lastDot);
        }
        Objects.requireNonNull(packageName, String.format(MESSAGE_TEMPLATE_VALIDATION_PACKAGE_NAME_BEAN_DEFINITION, clazzNameForBeanDefinition));

        String simpleClassNameOfBeanDefinition = clazzNameForBeanDefinition.substring(lastDot + 1);
        String beanDefinitionName = resolveBeanDefinitionNameForClassesWithSameSimpleClassName(clazzNameForBeanDefinition, simpleClassNameOfBeanDefinition);

        BeanDefinitionData beanDefinitionData = BeanDefinitionData.of(clazzNameForBeanDefinition, simpleClassNameOfBeanDefinition, packageName, beanDefinitionName);
        return defineBeanDefinitionIfAlreadyGenerated(beanDefinitionData);
    }

    private String resolveBeanDefinitionNameForClassesWithSameSimpleClassName(
            final String clazzNameForBeanDefinition,
            final String simpleClassNameOfBeanDefinition) {
        return sameBeanDefinitionClassNameHolder
                .getPrefixForSameBeanDefinitionClassName(clazzNameForBeanDefinition)
                .map(beanDefinitionNamePrefix -> buildBeanDefinitionName(simpleClassNameOfBeanDefinition, beanDefinitionNamePrefix))
                .orElse(buildBeanDefinitionName(simpleClassNameOfBeanDefinition,""));
    }

    private static String buildBeanDefinitionName(
            final String simpleClassNameOfBeanDefinition,
            final String beanDefinitionSuffix) {
        char firstCharInClassName = simpleClassNameOfBeanDefinition.charAt(INDEX_OF_FIRST_CHAR_IN_CLASS_NAME);
        String classNameWithoutFirstChar = simpleClassNameOfBeanDefinition.substring(INDEX_OF_SECOND_CHAR_IN_CLASS_NAME_FOR_SUBSTRING, simpleClassNameOfBeanDefinition.length());
        return Character.toLowerCase(firstCharInClassName) + classNameWithoutFirstChar + beanDefinitionSuffix;
    }

    private Optional<BeanDefinitionData> defineBeanDefinitionIfAlreadyGenerated(
            final BeanDefinitionData beanDefinitionData) {
        boolean alreadyGeneratedBeanDefinitionFound = false;
        if (alreadyGenerated.contains(beanDefinitionData)) {
            terminalPrinter.printNote(
                    String.format(
                            MESSAGE_TEMPLATE_ABOUT_ALREADY_GENERATED_BEAN_DEFINITION,
                            beanDefinitionData.getFullClazzName()));
            alreadyGeneratedBeanDefinitionFound = true;
        } else {
            alreadyGenerated.add(beanDefinitionData);
        }


        return alreadyGeneratedBeanDefinitionFound ? Optional.empty() : Optional.of(beanDefinitionData);
    }
}
