package ca.gbc.inventoryservice;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public class AbstractContainerBase {

    static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:latest")
            .withUsername("rootadmin")
            .withPassword("password");

    @BeforeAll
    public static void initialize() {
        POSTGRE_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", () -> "rootadmin");
        registry.add("spring.datasource.password", () -> "password");
    }
}