package ca.gbc.productservice;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.model.Product;
import ca.gbc.productservice.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.Module;

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

//    BDD - Behaviour Driven Development
//    Given - Setup
//    when - action
//    then - verify


    @Test
    void getAllProducts() throws Exception {
        //given
        productRepository.saveAll(getProductList()); //repository is a proxy to your db. saveall uses lists

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/product")
                .accept(MediaType.APPLICATION_JSON));

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andDo(MockMvcResultHandlers.print()); //you wouldnt use this for testing production

        MvcResult result = response.andReturn(); //should be JSON response
        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNodes = new ObjectMapper().readTree(jsonResponse);

        int actualSize = jsonNodes.size();
        int expectedSize = getProductList().size();

        Assertions.assertEquals(expectedSize, actualSize);



    }

    @Test
    void updateProduct() throws Exception {

        //given
        Product savedProduct = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Widget")
                .description(("Widget Original Price"))
                .price(BigDecimal.valueOf(100))
                .build();

        //saved product with original price
        productRepository.save(savedProduct);

        //prepare updated product
        savedProduct.setPrice(BigDecimal.valueOf(200)); //saves in memeory
        String productRequestString = objectMapper.writeValueAsString(savedProduct);

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/product" + savedProduct.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequestString));

        //then


    }

    @Test
    void deleteProduct() {

    }



}
