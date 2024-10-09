package {{root_package}}.integration.tests.config;

{% if feature-sqs -%}
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;{%- endif %}
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;{% if feature-sqs or feature-sns %}
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;{% endif %}
import {{root_package}}.client.{{ ProjectPrefix }}{{ ProjectSuffix }}Client;
import {{root_package}}.server.{{ ProjectPrefix }}{{ ProjectSuffix }}Server;
{% if feature-sqs or feature-sns %}
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.*;{% endif %}

@Configuration
public class IntegrationTestsConfig {
    {% if feature-sqs or feature-sns %}
    @Bean(initMethod = "start", destroyMethod = "stop")
    public LocalStackContainer localStack() {
        return new LocalStackContainer(DockerImageName.parse("localstack/localstack"))
                .withServices(SQS, SNS);
    }{% endif %}

    @Bean(initMethod = "start", destroyMethod = "stop")
    public {{ ProjectPrefix }}{{ ProjectSuffix }}Server {{ projectPrefix }}{{ ProjectSuffix }}Server() {
         return new {{ ProjectPrefix }}{{ ProjectSuffix }}Server()
                .withRandomPorts(){% if feature-sns %}
                .withProperty("sns.endpoint", localStack().getEndpointConfiguration(SNS).getServiceEndpoint()){% endif %}{% if feature-sqs %}
                .withProperty("sqs.endpoint", localStack().getEndpointConfiguration(SQS).getServiceEndpoint()){% endif %}
                .withTempDb();
    }

    @Bean
    public {{ ProjectPrefix }}{{ ProjectSuffix }}Client {{ projectPrefix }}{{ ProjectSuffix }}Client({{ ProjectPrefix }}{{ ProjectSuffix }}Server server) {
        return {{ ProjectPrefix }}{{ ProjectSuffix }}Client.of("localhost", server.getGrpcPort());
    }{% if feature-sqs %}

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate({{ ProjectPrefix }}{{ ProjectSuffix }}Server server) {
        return server.getContext().getBean(QueueMessagingTemplate.class);
    }{% endif %}
}
