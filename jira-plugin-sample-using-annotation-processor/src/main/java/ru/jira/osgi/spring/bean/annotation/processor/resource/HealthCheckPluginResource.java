package ru.jira.osgi.spring.bean.annotation.processor.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/healthcheck")
public class HealthCheckPluginResource {

    @GET
    public String healthCheck() {
        return "OK";
    }
}
