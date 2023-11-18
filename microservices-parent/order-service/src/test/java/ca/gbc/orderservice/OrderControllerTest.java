package ca.gbc.orderservice;

import ca.gbc.orderservice.dto.InventoryResponse;
import ca.gbc.orderservice.dto.OrderLineItemDto;
import ca.gbc.orderservice.dto.OrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.config.discovery.enabled=false",
        "inventory.service.url=http://localhost:8089" // WireMock server port
})
@AutoConfigureMockMvc
public class OrderControllerTest extends AbstractContainerBase {

    @Autowired
    private MockMvc mockMvc;

    private WireMockServer wireMockServer;

    //Using wiremock to simulate inventory-service
    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        // Setup stub for inventory service
        wireMockServer.stubFor(WireMock.post(WireMock.urlEqualTo("/"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"skuCode\":\"SKU123\",\"sufficientStock\":true}")
                        .withStatus(200)));
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testPlaceOrder() throws Exception {
        // Updated line items to match the request body you provided
        OrderLineItemDto lineItem1 = OrderLineItemDto.builder()
                .skuCode("sku_12345")
                .price(new BigDecimal("200"))
                .quantity(1)
                .build();

        OrderLineItemDto lineItem2 = OrderLineItemDto.builder()
                .skuCode("sku_55555")
                .price(new BigDecimal("1000"))
                .quantity(2)
                .build();

        OrderRequest orderRequest = new OrderRequest(Arrays.asList(lineItem1, lineItem2));

        ObjectMapper objectMapper = new ObjectMapper();
        String orderRequestJson = objectMapper.writeValueAsString(orderRequest);

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderRequestJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Order placed successfully!"));
    }

}
