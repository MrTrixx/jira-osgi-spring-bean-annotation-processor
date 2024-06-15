package ru.jira.osgi.spring.bean.annotation.processor.configuration;

import com.atlassian.event.api.EventPublisher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.jira.osgi.spring.bean.annotation.processor.service.PluginApplicationContextAccessor;
import ru.jira.osgi.spring.bean.annotation.processor.service.PluginBeanExplorerService;
import ru.jira.osgi.spring.bean.annotation.processor.service.PluginEventListener;

@Configuration
public class ServiceConfiguration {

    @Bean
    PluginEventListener pluginEventListener(final EventPublisher eventPublisher) {
        return new PluginEventListener(eventPublisher);
    }

    @Bean
    PluginApplicationContextAccessor applicationContextAccessor(final ApplicationContext applicationContext) {
        return new PluginApplicationContextAccessor(applicationContext);
    }

    @Bean
    PluginBeanExplorerService pluginBeanExplorerService(
            final PluginApplicationContextAccessor pluginApplicationContextAccessor) {
        return new PluginBeanExplorerService(pluginApplicationContextAccessor);
    }
}
