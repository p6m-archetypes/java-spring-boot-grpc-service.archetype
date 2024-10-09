package {{ root_package }}.server;

import org.lognet.springboot.grpc.autoconfigure.GRpcServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;
import {{ group_id }}.platform.tracing.TracingInitializer;

import javax.annotation.PreDestroy;
import java.util.*;

@Component
public class {{ ProjectPrefix }}{{ ProjectSuffix }}Server {

    final Logger log = LoggerFactory.getLogger({{ ProjectPrefix }}{{ ProjectSuffix }}Server.class);

    static {
        TracingInitializer.initialize();
        if (System.getProperty("logging.config") == null) {
            System.setProperty("logging.config", "classpath:{{ artifact-id}}-server-logback.xml");
        }
    }

    private static final String SPRING_CONFIG_NAME = "spring.config.name";
    private static final String SPRING_APPLICATION_NAME = "spring.application.name";
    private static final String SPRING_JMX_DEFAULT = "spring.jmx.default";
    private static final String SPRING_JMX_ENABLED = "spring.jmx.enabled";
    private static final String APPLICATION_NAME = "{{ artifact-id }}-server";

    private final SpringApplication springApplication;
    private final Properties overrides = new Properties();
    private ConfigurableApplicationContext context;
    private final List<String> arguments = new ArrayList<>();
    private boolean disableJmx = false;

    public {{ ProjectPrefix }}{{ ProjectSuffix }}Server() {
        this.springApplication = new SpringApplication({{ ProjectPrefix }}{{ ProjectSuffix }}ServerConfig.class);
        if (System.getProperty("logging-structured") != null
            || System.getenv("logging-structured") != null
            || System.getenv("LOGGING_STRUCTURED") != null
        ) {
            // Don't pollute the log stream with unstructured text.
            springApplication.setBannerMode(Banner.Mode.OFF);
        }
        springApplication.addInitializers(applicationContext -> applicationContext
                .getEnvironment()
                .getPropertySources()
                .addFirst(new PropertiesPropertySource("overrides", overrides)));
    }

    public {{ ProjectPrefix }}{{ ProjectSuffix }}Server start() {
        initializeSystemProperties();
        String[] args = new String[arguments.size()];
        args = arguments.toArray(args);
        this.context = springApplication.run(args);
        clearSystemProperties();
        return this;
    }

    public {{ ProjectPrefix }}{{ ProjectSuffix }}Server stop() {
        if (context != null) {
            context.close();
        }
        return this;
    }

    private void initializeSystemProperties() {
        System.setProperty(SPRING_CONFIG_NAME, APPLICATION_NAME);
        System.setProperty(SPRING_APPLICATION_NAME, APPLICATION_NAME);
        System.setProperty(SPRING_JMX_DEFAULT, APPLICATION_NAME);
        if (disableJmx) {
            System.setProperty(SPRING_JMX_ENABLED, "false");
        }
    }

    private void clearSystemProperties() {
        System.clearProperty(SPRING_CONFIG_NAME);
        System.clearProperty(SPRING_APPLICATION_NAME);
        System.clearProperty(SPRING_JMX_DEFAULT);
        if (disableJmx) {
            System.clearProperty(SPRING_JMX_ENABLED);
        }
    }

    public {{ ProjectPrefix }}{{ ProjectSuffix }}Server disableJmx() {
        this.disableJmx = true;
        return this;
    }

    public {{ ProjectPrefix }}{{ ProjectSuffix }}Server withArguments(String... args) {
        arguments.addAll(Arrays.asList(args));
        return this;
    }

    public {{ ProjectPrefix }}{{ ProjectSuffix }}Server withProperty(String key, String value) {
        this.overrides.setProperty(key, value);
        return this;
    }

    public {{ ProjectPrefix }}{{ ProjectSuffix }}Server withRandomPorts() {
        return withProperty("server.port", "0")
              .withProperty("grpc.port", "0")
              ;
    }

    public {{ ProjectPrefix }}{{ ProjectSuffix }}Server withTempDb()  {
        return withProperty("temp-db", "true");
    }

    public ApplicationContext getContext() {
        if (context == null) {
            throw new RuntimeException("ApplicationContext has not been initialized, yet!");
        }
        return context;
    }

    public static void main(String[] args) {
        new {{ ProjectPrefix }}{{ ProjectSuffix }}Server().withArguments(args).start();
    }

    public int getManagementPort() {
        Integer serverPort = context.getEnvironment().getProperty("local.server.port", Integer.class);
        if (serverPort == null) {
            throw new RuntimeException("Server not started!");
        }
        return serverPort;
    }

    public int getGrpcPort() {
        Integer grpcPort = context.getBean(GRpcServerProperties.class).getRunningPort();
        if (grpcPort == null) {
            throw new RuntimeException("gRPC not started!");
        }
        return grpcPort;
    }

    @PreDestroy
    public void preDestroy(){
        log.info("Shutting down the server...");
        stop();
        log.info("Server stopped.");
    }
}
