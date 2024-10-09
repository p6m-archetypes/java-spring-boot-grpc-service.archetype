package {{ root_package }}.core;
{% if feature-sns %}
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;{% endif %}{% if feature-sqs %}
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;{% endif %}{% if persistence != 'None' -%}
import {{ root_package }}.persistence.{{ ProjectPrefix }}{{ ProjectSuffix }}PersistenceConfig;{% endif %}
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;{% if feature-sns %}
import {{ group_id }}.platform.aws.SnsClientConfig;
import {{ group_id }}.platform.aws.SnsProperties;{% endif %}{% if feature-sqs %}
import {{ group_id }}.platform.aws.SqsClientConfig;
import {{ group_id }}.platform.aws.SqsProperties;{% endif %}

@Configuration
@Import({{'{'}}{% if persistence != 'None' %}
        {{ ProjectPrefix }}{{ ProjectSuffix }}PersistenceConfig.class,{% endif %}{% if feature-sns %}
        SnsClientConfig.class,{% endif %}{% if feature-sqs %}
        SqsClientConfig.class,{% endif %}
})
@ComponentScan
public class {{ ProjectPrefix }}{{ ProjectSuffix }}CoreConfig {

}
