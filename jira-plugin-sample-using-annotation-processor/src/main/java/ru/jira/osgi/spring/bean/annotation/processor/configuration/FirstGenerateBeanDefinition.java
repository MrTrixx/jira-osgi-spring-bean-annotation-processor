package ru.jira.osgi.spring.bean.annotation.processor.configuration;

import com.atlassian.jira.bc.customfield.CustomFieldService;
import com.atlassian.jira.bc.group.GroupService;
import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.config.properties.ApplicationProperties;
import ru.jira.osgi.java.config.annotation.processor.annotation.GenerateOsgiImportConfigurationClass;

@GenerateOsgiImportConfigurationClass(classes = {
        ApplicationProperties.class,
        com.atlassian.sal.api.ApplicationProperties.class,
        IssueService.class,
        GroupService.class,
        CustomFieldService.class
})
public class FirstGenerateBeanDefinition {}