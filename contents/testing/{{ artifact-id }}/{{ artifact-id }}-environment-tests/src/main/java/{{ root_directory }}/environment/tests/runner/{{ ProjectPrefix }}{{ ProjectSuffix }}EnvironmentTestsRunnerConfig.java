package {{ root_package }}.environment.tests.runner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import {{ root_package }}.client.{{ ProjectPrefix }}{{ ProjectSuffix }}Client;
import {{ root_package }}.environment.tests.runner.{{ ProjectPrefix }}{{ ProjectSuffix }}EnvironmentTestsRunner.Executor;

@Configuration
public class {{ ProjectPrefix }}{{ ProjectSuffix }}EnvironmentTestsRunnerConfig {

    @Bean
    Executor testsExecutor(@Value("${execution.environment}") String environment) {
        return new Executor(environment);
    }

}
