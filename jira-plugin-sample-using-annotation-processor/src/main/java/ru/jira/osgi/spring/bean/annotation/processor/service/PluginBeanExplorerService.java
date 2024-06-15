package ru.jira.osgi.spring.bean.annotation.processor.service;

import ru.jira.osgi.spring.bean.annotation.processor.dto.BeanDefinitionData;

import java.util.List;

public class PluginBeanExplorerService {
    private final PluginApplicationContextAccessor pluginApplicationContextAccessor;

    public PluginBeanExplorerService(final PluginApplicationContextAccessor pluginApplicationContextAccessor) {
        this.pluginApplicationContextAccessor = pluginApplicationContextAccessor;
    }

    public List<BeanDefinitionData> exploreAllBeans() {
        return pluginApplicationContextAccessor.getPluginBeans();
    }
}
