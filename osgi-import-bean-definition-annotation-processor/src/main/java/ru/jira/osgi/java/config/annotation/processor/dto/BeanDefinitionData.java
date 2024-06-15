package ru.jira.osgi.java.config.annotation.processor.dto;

import java.util.Objects;

public class BeanDefinitionData {
    private final String fullClazzName;
    private final String simpleClazzName;
    private final String packageName;
    private final String beanDefinitionName;

    private BeanDefinitionData(
            final String fullClazzName,
            final String simpleClazzName,
            final String packageName,
            final String beanDefinitionName) {
        this.fullClazzName = Objects.requireNonNull(fullClazzName, "fullClazzName");
        this.simpleClazzName = Objects.requireNonNull(simpleClazzName, "clazzName");
        this.packageName = Objects.requireNonNull(packageName, "packageName");
        this.beanDefinitionName = Objects.requireNonNull(beanDefinitionName, "beanDefinitionName");
    }

    public static BeanDefinitionData of(
            final String fullClazzName,
            final String clazzName,
            final String packageName,
            final String beanDefinitionName) {
        return new BeanDefinitionData(fullClazzName, clazzName, packageName, beanDefinitionName);
    }

    public String getFullClazzName() {
        return fullClazzName;
    }

    public String getSimpleClazzName() {
        return simpleClazzName;
    }
    public String getBeanDefinitionName() {
        return beanDefinitionName;
    }

    public String getPackageName() {
        return packageName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final BeanDefinitionData that = (BeanDefinitionData) o;
        return Objects.equals(fullClazzName, that.fullClazzName) && Objects.equals(simpleClazzName, that.simpleClazzName) && Objects.equals(packageName, that.packageName) && Objects.equals(beanDefinitionName, that.beanDefinitionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullClazzName, simpleClazzName, packageName, beanDefinitionName);
    }
}
