package ca.gbc.orderservice;

import ca.gbc.orderservice.dto.OrderLineItemDto;
import ca.gbc.orderservice.dto.OrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers

public class OrderControllerTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testPlaceOrder() throws Exception {
        OrderLineItemDto lineItem = OrderLineItemDto.builder()
                .skuCode("SKU123")
                .price(new BigDecimal("10.00"))
                .quantity(2)
                .build();

        OrderRequest orderRequest = new OrderRequest(Arrays.asList(lineItem));

        ObjectMapper objectMapper = new ObjectMapper();
        String orderRequestJson = objectMapper.writeValueAsString(orderRequest);

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderRequestJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Order placed successfully!"));
    }
}
