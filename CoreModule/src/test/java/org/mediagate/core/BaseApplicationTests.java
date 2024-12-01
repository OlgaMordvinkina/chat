//package org.mediagate.chat;
//
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.util.TestPropertyValues;
//import org.springframework.context.ApplicationContextInitializer;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//@Testcontainers
//@SpringBootTest(classes = ChatApplication.class)
//@ContextConfiguration(initializers = {BaseApplicationTests.Initializer.class})
//@ActiveProfiles({"test"})
//@AutoConfigureMockMvc
//public abstract class BaseApplicationTests {
//    private static final String DATABASE_NAME = "test_chat";
//
//    @Container
//    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12.3")
//            .withReuse(true)
//            .withDatabaseName(DATABASE_NAME);
//
//    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
//            TestPropertyValues.of(
//                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
//                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
//                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl()
//            ).applyTo(configurableApplicationContext.getEnvironment());
//        }
//    }
//}
