package ru.jira.osgi.spring.bean.annotation.processor.configuration;

import com.atlassian.event.api.EventPublisher;
import com.atlassian.plugins.osgi.javaconfig.OsgiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NonGeneratedImportOsgiComponentConfiguration {

    @Bean
    EventPublisher eventPublisher() {
        return OsgiServices.importOsgiService(EventPublisher.class);
    }


}
