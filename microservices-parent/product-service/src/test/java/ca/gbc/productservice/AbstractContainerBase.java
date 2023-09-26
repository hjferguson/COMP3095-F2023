package ca.gbc.productservice;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
public class AbstractContainerBase extends ProductServiceApplicationTests{

    static final MongoDBContainer MONGO_DB_CONTAINER;

    static {
        MONGO_DB_CONTAINER = new MongoDBContainer("mongo:latest");
        MONGO_DB_CONTAINER.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.data.mongodb.uri",
                MONGO_DB_CONTAINER::getReplicaSetUrl);
    }

}
