package {{ root_package }}.environment.tests.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import {{ root_package }}.client.{{ ProjectPrefix }}{{ ProjectSuffix }}Client;

@Configuration
public class {{ ProjectPrefix }}{{ ProjectSuffix }}EnvironmentTestsConfig {

    @Bean
    public {{ ProjectPrefix }}{{ ProjectSuffix }}Client {{ projectPrefix }}{{ ProjectSuffix }}Client(
            @Value("${clients.{{ artifact-id }}.host}") String host,
            @Value("${clients.{{ artifact-id }}.port}") int port) {
        return {{ ProjectPrefix }}{{ ProjectSuffix }}Client.of(host, port);
    }
}
