package ca.gbc.productservice;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.model.Product;
import ca.gbc.productservice.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductServiceApplicationTests extends AbstractContainerBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .name("Zojirushi Rice Cooker")
                .description("Never make shit rice again!")
                .price(BigDecimal.valueOf(220))
                .build();
    }

    private List<Product> getProductList() {
        List<Product> productList = new ArrayList<>();
        UUID uuid = UUID.randomUUID();

        Product product = Product.builder()
                .id(uuid.toString())
                .name("Zojirushi Rice Cooker")
                .description("Never make shit rice again!")
                .price(BigDecimal.valueOf(220))
                .build();

        productList.add(product);
        return productList;
    }

    private String convertObjectToJson(List<ProductResponse> productList) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(productList);
    }

    private List<ProductResponse> convertJsonToObject(String jsonString) throws Exception {
        return objectMapper.readValue(jsonString, new TypeReference<List<ProductResponse>>() {
        });
    }

    @Test
    void createProducts() throws Exception {
        ProductRequest productRequest = getProductRequest();
        String productRequestString = objectMapper.writeValueAsString(productRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        System.out.println("productRepository.findAll().size() " + productRepository.findAll().size());

        Assertions.assertTrue(productRepository.findAll().size() > 0);

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("Zojirushi Rice Cooker"));

        List<Product> products = mongoTemplate.find(query, Product.class);
        Assertions.assertTrue(products.size() > 0);
    }


    @Test
    void getProductById() throws Exception {
        // Setup
        productRepository.saveAll(getProductList());

        // Action
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/products")
                .accept(MediaType.APPLICATION_JSON));

        // Verify
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andDo(MockMvcResultHandlers.print());

        MvcResult result = response.andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNodes = new ObjectMapper().readTree(jsonResponse);

        int actualSize = jsonNodes.size();
        int expectedSize = getProductList().size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    void updateProduct() throws Exception {

        // Prepare saved product
        Product savedProduct = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Widget")
                .description("Widget Original Price")
                .price(BigDecimal.valueOf(100))
                .build();

        // Saved product
        productRepository.save(savedProduct);

        // Prepare updated product and productRequest
        savedProduct.setPrice(BigDecimal.valueOf(200));
        String productRequestString = objectMapper.writeValueAsString(savedProduct);

        // Action
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/products/" + savedProduct.getId()) // Fixed the URL to match the new base URL
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequestString));

        // Verify
        response.andExpect(MockMvcResultMatchers.status().isNoContent()); // Changed to 204 No Content
        response.andDo(MockMvcResultHandlers.print());

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(savedProduct.getId()));
        Product storedProduct = mongoTemplate.findOne(query, Product.class);

        assertEquals(savedProduct.getId(), storedProduct.getId());
    }

    @Test
    void deleteProduct() throws Exception {

        // Prepare saved product
        Product savedProduct = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Asus Laptop")
                .description("Laptop Original Price")
                .price(BigDecimal.valueOf(1000))
                .build();

        // Saved product
        productRepository.save(savedProduct);

        // Action
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/products/" + savedProduct.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // Verify
        response.andExpect(MockMvcResultMatchers.status().isNoContent());
        response.andDo(MockMvcResultHandlers.print());
    }
}