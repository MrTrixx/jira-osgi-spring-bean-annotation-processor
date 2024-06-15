package ru.jira.osgi.spring.bean.annotation.processor.service;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.plugin.event.events.PluginEnabledEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

@Slf4j
public class PluginEventListener implements InitializingBean, DisposableBean {
    private static final String CURRENT_PLUGIN_KEY = "ru.jira.osgi.spring.bean.annotation.processor.jira-plugin-sample-using-annotation-processor";

    private final EventPublisher eventPublisher;

    public PluginEventListener(final EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @EventListener
    public void listen(final PluginEnabledEvent pluginEnabledEvent) {
        if (CURRENT_PLUGIN_KEY.equals(pluginEnabledEvent.getPlugin().getKey())) {
            log.debug("Plugin {} enabled", CURRENT_PLUGIN_KEY);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        eventPublisher.register(this);
    }

    @Override
    public void destroy() throws Exception {
        eventPublisher.unregister(this);
    }
}
