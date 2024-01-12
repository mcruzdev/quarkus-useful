package dev.matheuscruz.quarkus.useful.runtime;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class ConsoleRecorder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleRecorder.class);

    public void log(List<String> logs) {
        for (String l : logs) {
            LOGGER.info(l);
        }
    }
}
