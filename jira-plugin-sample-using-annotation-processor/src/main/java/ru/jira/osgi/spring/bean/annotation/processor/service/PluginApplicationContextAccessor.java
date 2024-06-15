package ru.jira.osgi.spring.bean.annotation.processor.service;

import org.springframework.context.ApplicationContext;
import ru.jira.osgi.spring.bean.annotation.processor.dto.BeanDefinitionData;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class PluginApplicationContextAccessor {
    private final ApplicationContext pluginApplicationContext;

    public PluginApplicationContextAccessor(final ApplicationContext pluginApplicationContext) {
        this.pluginApplicationContext = pluginApplicationContext;
    }

    public List<BeanDefinitionData> getPluginBeans()  {
        String[] beanDefinitionNames = pluginApplicationContext.getBeanDefinitionNames();
        List<BeanDefinitionData> beanDefinitions = new ArrayList<>();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = pluginApplicationContext.getBean(beanDefinitionName);
            String beanClassName;
            if (isJDKDynamicProxy(bean)) {
                beanClassName = bean.toString();
            } else {
                beanClassName = bean.getClass().getCanonicalName();
            }
            beanDefinitions.add(BeanDefinitionData.builder()
                                                  .beanDefinitionClassName(beanClassName)
                                                  .beanDefinitionName(beanDefinitionName)
                                                  .build());
        }
        return beanDefinitions;
    }

    private static boolean isJDKDynamicProxy(final Object bean) {
        return Proxy.isProxyClass(bean.getClass());
    }

}
