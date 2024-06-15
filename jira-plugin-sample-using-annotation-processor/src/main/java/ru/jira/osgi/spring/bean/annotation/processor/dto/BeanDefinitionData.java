package ru.jira.osgi.spring.bean.annotation.processor.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;

@Getter
@RequiredArgsConstructor
@Builder
public class BeanDefinitionData {
    @JsonProperty
    private final String beanDefinitionName;
    @JsonProperty
    private final String beanDefinitionClassName;
}
