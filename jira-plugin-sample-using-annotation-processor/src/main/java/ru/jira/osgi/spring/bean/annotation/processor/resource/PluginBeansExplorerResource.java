package ru.jira.osgi.spring.bean.annotation.processor.resource;

import lombok.RequiredArgsConstructor;
import ru.jira.osgi.spring.bean.annotation.processor.dto.BeanDefinitionData;
import ru.jira.osgi.spring.bean.annotation.processor.service.PluginBeanExplorerService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/bean/explorer")
@RequiredArgsConstructor
public class PluginBeansExplorerResource {
    private final PluginBeanExplorerService pluginBeanExplorerService;

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public List<BeanDefinitionData> exploreAllPluginBeans() {
        return pluginBeanExplorerService.exploreAllBeans();
    }
}
