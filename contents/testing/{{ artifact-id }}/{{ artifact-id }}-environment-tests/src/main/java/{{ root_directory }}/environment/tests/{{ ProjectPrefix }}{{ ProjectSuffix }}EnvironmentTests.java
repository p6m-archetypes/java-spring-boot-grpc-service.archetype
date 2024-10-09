package {{ root_package }}.environment.tests;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import {{ root_package }}.client.{{ ProjectPrefix }}{{ ProjectSuffix }}Client;
import {{ root_package }}.environment.tests.annotations.AllEnvironments;
import {{ root_package }}.environment.tests.config.{{ ProjectPrefix }}{{ ProjectSuffix }}EnvironmentTestsConfig;

@SpringBootTest(properties = {
        "spring.config.name={{ artifact-id }}-environment-tests",
        "spring.main.banner-mode=off"
}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@SpringJUnitConfig(
        classes = {{'{'}}{{ ProjectPrefix }}{{ ProjectSuffix }}EnvironmentTestsConfig.class}
)
public class {{ ProjectPrefix }}{{ ProjectSuffix }}EnvironmentTests {

    private static final Logger logger = LoggerFactory.getLogger({{ ProjectPrefix }}{{ ProjectSuffix }}EnvironmentTests.class);

    @Autowired
    {{ ProjectPrefix }}{{ ProjectSuffix }}Client {{ projectPrefix }}{{ ProjectSuffix }}Client;

    @Test
    @AllEnvironments
    public void test() {
        logger.info("It works!");
    }
}
