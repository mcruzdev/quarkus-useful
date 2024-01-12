package dev.matheuscruz.quarkus.useful.it;

import dev.matheuscruz.quarkus.useful.runtime.GreetingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/greetings")
public class GreetingResource {

    @Inject
    GreetingService greetingService;

    @GET
    public String greeting() {
        return greetingService.message();
    }
}