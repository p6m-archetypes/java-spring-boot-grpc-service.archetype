package {{ root_package }}.persistence;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;{% if persistence == 'CockroachDB' %}
import org.testcontainers.containers.CockroachContainer;{% endif %}
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.utility.DockerImageName;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Optional;
import java.util.Properties;

/**
 * @author Archetect
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {
                "{{root_package}}.persistence.repositories",
        },
        entityManagerFactoryRef = "{{projectPrefix}}EMF",
        transactionManagerRef = "{{projectPrefix}}TM")
@ComponentScan
public class {{ ProjectPrefix }}{{ ProjectSuffix }}PersistenceConfig {
    private static final Logger logger = LoggerFactory.getLogger({{ ProjectPrefix }}{{ ProjectSuffix }}PersistenceConfig.class );
    private final Environment env;{%- if persistence == 'CockroachDB' %}
    private static final String COCKROACH_DB_IMAGE = "cockroachdb/cockroach:v22.1.0";{% endif %}

    @Autowired
    public {{ ProjectPrefix }}{{ ProjectSuffix }}PersistenceConfig(final Environment env) {
        this.env = env;
    }

    @Bean(name = "{{projectPrefix}}TM")
    @Qualifier("{{projectPrefix}}")
    public JpaTransactionManager {{projectPrefix}}TM(
    @Qualifier("{{projectPrefix}}DS") final DataSource dataSource,
    @Qualifier("{{projectPrefix}}EMF") final EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource);
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean {{projectPrefix}}EMF(
    @Qualifier("{{projectPrefix}}DS") final DataSource dataSource,
    @Qualifier("{{projectPrefix}}VA") final JpaVendorAdapter vendorAdapter) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPersistenceUnitName("{{projectPrefix}}");
        factory.setPackagesToScan(
            "{{root_package}}.persistence.entities"
        );
        Properties props = new Properties();
        props.put("hibernate.auto_quote_keyword", "true");
        factory.setJpaProperties(props);
        return factory;
    }

    @Bean
    @Qualifier("{{projectPrefix}}")
    public JdbcTemplate {{projectPrefix}}JdbcTemplate(@Qualifier("{{projectPrefix}}DS") final DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

    @Bean
    @ConditionalOnProperty(name = "liquibase", havingValue = "true", matchIfMissing = true)
    public SpringLiquibase {{projectPrefix}}Liquibase(@Qualifier("{{projectPrefix}}DS") final DataSource dataSource) {
        logger.info("Applying Liquibase");
        SpringLiquibase liquibase = new SpringLiquibase();
//         liquibase.setContexts(RuntimeMode.current().name());
        liquibase.setDataSource(dataSource);
        if (env.containsProperty("initdb") || env.containsProperty("gateway.initdb")) {
            liquibase.setDropFirst(true);
        }
        liquibase.setChangeLog("classpath:db/{{artifact-id}}/changelog-master.xml");
        return liquibase;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnProperty(name = "temp-db")
    public JdbcDatabaseContainer<?>  databaseContainer() {
        {%- if persistence == 'CockroachDB' %}
        return new CockroachContainer(DockerImageName.parse(COCKROACH_DB_IMAGE));{% endif %}
    }

    @Bean
    public DataSource {{ projectPrefix }}DS(Optional<JdbcDatabaseContainer<?>> databaseContainer) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setPoolName("{{ projectPrefix }}");
        logger.info("Configuring {{ project-title }} DataSource");
        if (databaseContainer.isPresent()) {
            logger.info("{{ project-title }} JDBC URL: {}", databaseContainer.get().getJdbcUrl());
            dataSource.setJdbcUrl(databaseContainer.get().getJdbcUrl());
            dataSource.setUsername(databaseContainer.get().getUsername());
            dataSource.setPassword(databaseContainer.get().getPassword());
        } else {
            dataSource.setJdbcUrl(env.getRequiredProperty("persistence.database.url"));
            dataSource.setUsername(env.getRequiredProperty("persistence.database.username"));
            dataSource.setPassword(env.getRequiredProperty("persistence.database.password"));
        }
        if (env.containsProperty("persistence.pool.maximumPoolSize")) {
            dataSource.setMaximumPoolSize(env.getProperty("persistence.pool.maximumPoolSize", Integer.class));
        }
        if (env.containsProperty("persistence.pool.connectionTimeout")) {
            dataSource.setConnectionTimeout(env.getProperty("persistence.pool.connectionTimeout", Long.class));
        }
        if (env.containsProperty("persistence.pool.maxLifetime")) {
            dataSource.setMaxLifetime(env.getProperty("persistence.pool.maxLifetime", Long.class));
        }
        if (env.containsProperty("persistence.pool.idleTimeout")) {
            dataSource.setIdleTimeout(env.getProperty("persistence.pool.idleTimeout", Long.class));
        }
        return dataSource;
    }

    @Bean
    public HibernateJpaVendorAdapter {{ projectPrefix }}VA() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.POSTGRESQL);
//         vendorAdapter.setShowSql(Switches.showSql.isEnabled());
        return vendorAdapter;
    }
}