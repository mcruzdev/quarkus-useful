package dev.matheuscruz.quarkus.useful.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class QuarkusUsefulProcessor {

    private static final String FEATURE = "quarkus-useful";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
