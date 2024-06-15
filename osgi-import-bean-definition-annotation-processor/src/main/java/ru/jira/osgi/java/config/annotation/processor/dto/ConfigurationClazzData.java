package ru.jira.osgi.java.config.annotation.processor.dto;

import java.util.List;
import java.util.Objects;

public class ConfigurationClazzData {
    private final String sourceAnnotatedClassName;
    private final List<BeanDefinitionData> beanDefinitionData;

    private ConfigurationClazzData(
            final String sourceAnnotatedClassName,
            final List<BeanDefinitionData> beanDefinitionData) {
        this.sourceAnnotatedClassName = Objects.requireNonNull(sourceAnnotatedClassName, "sourceAnnotatedClassName");
        this.beanDefinitionData = Objects.requireNonNull(beanDefinitionData, "beanDefinitionData");
    }


    public static ConfigurationClazzData of(final String sourceAnnotatedClassName,
            final List<BeanDefinitionData> beanDefinitionData) {
        return new ConfigurationClazzData(sourceAnnotatedClassName, beanDefinitionData);
    }

    public String getSourceAnnotatedClassName() {
        return sourceAnnotatedClassName;
    }

    public List<BeanDefinitionData> getBeanDefinitionData() {
        return beanDefinitionData;
    }
}
