package dev.matheuscruz.quarkus.useful.deployment;

import java.util.List;
import java.util.stream.Collectors;

import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.matheuscruz.quarkus.useful.runtime.ConsoleRecorder;
import dev.matheuscruz.quarkus.useful.runtime.GreetingService;
import dev.matheuscruz.quarkus.useful.runtime.NotifyStartingEventRecorder;
import dev.matheuscruz.quarkus.useful.runtime.UsefulConfiguration;
import io.quarkus.arc.deployment.GeneratedBeanBuildItem;
import io.quarkus.arc.deployment.GeneratedBeanGizmoAdaptor;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.ApplicationIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.MethodCreator;
import jakarta.enterprise.context.ApplicationScoped;

class QuarkusUsefulProcessor {

    private static final String FEATURE = "quarkus-useful";

    private static final Logger LOGGER = LoggerFactory.getLogger(QuarkusUsefulProcessor.class);

    private static final DotName POST_DOT_NAME = DotName.createSimple("jakarta.ws.rs.POST");
    private static final DotName GET_DOT_NAME = DotName.createSimple("jakarta.ws.rs.GET");
    private static final DotName PUT_DOT_NAME = DotName.createSimple("jakarta.ws.rs.PUT");
    private static final DotName DELETE_DOT_NAME = DotName.createSimple("jakarta.ws.rs.DELETE");

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    @Record(value = ExecutionTime.RUNTIME_INIT)
    void recordNotifyStartingEventRecorder(UsefulConfiguration config,
            NotifyStartingEventRecorder recorder) {
        // the following call will be recorded
        recorder.notify(config);
    }

    @BuildStep
    void generateGreetingService(BuildProducer<GeneratedBeanBuildItem> generatedClasses) {
        GeneratedBeanGizmoAdaptor gizmoAdapter = new GeneratedBeanGizmoAdaptor(generatedClasses);
        try (ClassCreator classCreator = ClassCreator.builder()
                .className("dev.matheuscruz.quarkus.useful.deployment.UsefulGreetingService")
                .interfaces(GreetingService.class)
                .classOutput(gizmoAdapter)
                .build()) {

            classCreator.addAnnotation(ApplicationScoped.class);

            try (MethodCreator returnHello = classCreator.getMethodCreator("message",
                    String.class)) {
                returnHello.setModifiers(Opcodes.ACC_PUBLIC);
                returnHello.returnValue(returnHello.load("Hello from Quarkus Useful extension"));
            }
        }
    }

    @BuildStep
    @Record(value = ExecutionTime.STATIC_INIT)
    void countJaxRs(ApplicationIndexBuildItem jandex, ConsoleRecorder consoleRecorder) {
        Index index = jandex.getIndex();
        List<String> logs = List.of(POST_DOT_NAME, GET_DOT_NAME, PUT_DOT_NAME, DELETE_DOT_NAME)
                .stream()
                .map(dotName -> {
                    int size = index.getAnnotations(dotName).size();
                    LOGGER.info("This is called during build time");
                    return String.format("Your application has %d method(s) annotated with %s", size,
                            dotName.toString());
                })
                .collect(Collectors.toList());

        // The following code is recorded to be runned at runtime.
        consoleRecorder.log(logs);
    }
}
