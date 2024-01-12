package dev.matheuscruz.quarkus.useful.runtime;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "useful", phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
public class UsefulConfiguration {

    /**
     * The listener URL to be notified about the 'starting' event of the
     * application.
     */
    @ConfigItem(name = "listenerUrl")
    public Optional<String> listenerUrl;
}