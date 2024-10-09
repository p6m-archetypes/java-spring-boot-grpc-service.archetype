package {{ root_package }}.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.annotation.Import;
import {{ root_package }}.core.{{ ProjectPrefix }}{{ ProjectSuffix }}CoreConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
{% if feature-sqs %}import {{ group_id }}.platform.aws.SqsListenerConfig;{% endif %}

@SpringBootApplication(
        exclude = {LiquibaseAutoConfiguration.class, DataSourceAutoConfiguration.class}
)
@Import({
        {{ ProjectPrefix }}{{ ProjectSuffix }}CoreConfig.class,{% if feature-sqs %}
        SqsListenerConfig.class,{% endif %}
})
public class {{ ProjectPrefix }}{{ ProjectSuffix }}ServerConfig {

}
