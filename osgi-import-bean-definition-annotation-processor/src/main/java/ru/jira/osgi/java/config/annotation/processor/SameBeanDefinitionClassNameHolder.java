package ru.jira.osgi.java.config.annotation.processor;

import ru.jira.osgi.java.config.annotation.processor.util.Const;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SameBeanDefinitionClassNameHolder {
    private static final Map<String, String> SAME_BEAN_DEFINITION_CLASS_NAME_AND_BEAN_DEFINITION_PREFIX_MAPPING = new HashMap<>();

    static {
        SAME_BEAN_DEFINITION_CLASS_NAME_AND_BEAN_DEFINITION_PREFIX_MAPPING.put(Const.APPLICATION_PROPERTIES_CLASS_FROM_JIRA_CONFIG, "FromJiraConfig");
        SAME_BEAN_DEFINITION_CLASS_NAME_AND_BEAN_DEFINITION_PREFIX_MAPPING.put(Const.APPLICATION_PROPERTIES_CLASS_FROM_SAL_API, "FromSalApi");
    }

    public Optional<String> getPrefixForSameBeanDefinitionClassName(String beanDefinitionNameClassName) {
        return Optional.ofNullable(SAME_BEAN_DEFINITION_CLASS_NAME_AND_BEAN_DEFINITION_PREFIX_MAPPING.get(beanDefinitionNameClassName));
    }
}
