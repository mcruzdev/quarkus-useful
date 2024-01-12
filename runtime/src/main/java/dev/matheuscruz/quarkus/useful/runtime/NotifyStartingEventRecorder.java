package dev.matheuscruz.quarkus.useful.runtime;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;

import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.annotations.Recorder;

@Recorder //
public class NotifyStartingEventRecorder {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyStartingEventRecorder.class);

    public void notify(UsefulConfiguration config) { //
        if (config.listenerUrl.isEmpty()) {
            LOGGER.warn(
                    "You are using the 'quarkus-useful' extension but the configuration property quarkus.useful.listenerUrl not defined");
            return;
        }

        String applicationName = ConfigProvider.getConfig() //
                .getConfigValue("quarkus.application.name")
                .getValue();

        String body = String.format("{ \"applicationName\": \"%s\" }", applicationName);
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(config.listenerUrl.get()))
                .POST(BodyPublishers.ofString(body))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        CompletableFuture<HttpResponse<String>> httpResponse = httpClient.sendAsync(httpRequest,
                BodyHandlers.ofString());

        httpResponse.thenAccept(t -> {
            LOGGER.info("The quarkus-useful-extension gets the HTTP status code: {}", t.statusCode());
        });
    }
}