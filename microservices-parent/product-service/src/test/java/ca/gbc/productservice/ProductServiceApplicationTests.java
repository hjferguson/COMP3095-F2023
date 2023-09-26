package ca.gbc.productservice;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.model.Product;
import ca.gbc.productservice.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests extends AbstractContainerBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    MongoTemplate mongoTemplate;


    private ProductRequest getProductRequest(){
        return ProductRequest.builder()
                .name("Shiny Hat Rack")
                .description("A hat rack with a disconcerting sheen.")
                .price(BigDecimal.valueOf(34.99))
                .build();
    }

    private List<Product> getProductList(){
        List <Product> productList = new ArrayList<>();
        UUID uuid= UUID.randomUUID();

        Product product = Product.builder()
                .id(uuid.toString())
                .name("Shiny Hat Rack")
                .description("A hat rack with a disconcerting sheen.")
                .price(BigDecimal.valueOf(34.99))
                .build();

        productList.add(product);
        return productList;
    }


    private String convertObjectToJsonString(List <ProductResponse> productList) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(productList);
    }

    private List<ProductResponse> convertJSONStringToObject(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<List<ProductResponse>>(){});
    }

    @Test
    void createProduct()   throws Exception{
        ProductRequest productRequest = getProductRequest();
        String productRequestJson = objectMapper.writeValueAsString(productRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType("application/json")
                        .content(productRequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Assertions.assertTrue(productRepository.findAll().size() == 1);

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("Shiny Hat Rack"));
        List<Product> products = mongoTemplate.find(query, Product.class);
        Assertions.assertTrue(products.size() == 1);

    }

    @Test
    void getAllProducts() {


    }

    @Test
    void updateProduct() {

    }

    @Test
    void deleteProduct() {

    }



}
