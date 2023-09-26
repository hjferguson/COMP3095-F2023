package ca.gbc.productservice;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.model.Product;
import ca.gbc.productservice.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    private ProductRequest getProductRequest(){ //helper functions should be private

        return ProductRequest.builder()
                .name("Apple iPad 2023")
                .description("Apple iPad version 2023")
                .price(BigDecimal.valueOf(1200))
                .build();
    }

    private List<Product> getProductList() {
        List<Product> productList = new ArrayList<>();
        UUID uuid = UUID.randomUUID();

        Product product = Product.builder()
                .id(uuid.toString())
                .name("Apple iPad 2023")
                .description("apple ipad version 2023")
                .price(BigDecimal.valueOf(1200))
                .build();

        productList.add(product);
        return productList;
    }

    private String convertObjectToJson(List<ProductResponse> productList)
            throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(productList); //might have imported wrong object mapper
    }

    private List<ProductResponse> convertJsonStringToObject(String json)
            throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<List<ProductResponse>>() {
        });
    }

    @Test
    void createProduct(){

    }

    @Test
    void getAllProducts(){

    }

    @Test
    void updateProducts(){

    }

    @Test
    void deleteProduct(){

    }


}
