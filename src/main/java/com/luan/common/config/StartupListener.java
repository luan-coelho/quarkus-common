package com.luan.common.config;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class StartupListener {

    private static final Logger LOGGER = Logger.getLogger(StartupListener.class);

    @ConfigProperty(name = "quarkus.application.version")
    String quarkusVersion;

    @ConfigProperty(name = "java.version")
    String javaVersion;

    @ConfigProperty(name = "quarkus.profile")
    String environment;

    @ConfigProperty(name = "quarkus.http.port")
    int port;

    @ConfigProperty(name = "user.timezone")
    String timezone;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("\n\n" +
                    "╔════════════════════════════════════════════════════════════════════════╗\n" +
                    "║                          APPLICATION STARTED!                          ║\n" +
                    "╠════════════════════════════════════════════════════════════════════════╣\n" +
                    "\n" +
                    "    -> Quarkus Version      : " + quarkusVersion + "\n" +
                    "    -> Java Version         : " + javaVersion + "\n" +
                    "    -> Environment          : " + environment + "\n" +
                    "    -> HTTP Port            : " + port + "\n" +
                    "    -> Application Timezone : " + timezone + "\n" +
                    "\n" +
                    "╚════════════════════════════════════════════════════════════════════════╝\n"
        );
    }
}
