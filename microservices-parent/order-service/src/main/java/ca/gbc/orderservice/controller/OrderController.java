package ca.gbc.orderservice.controller;

import ca.gbc.orderservice.dto.OrderRequest;
import ca.gbc.orderservice.service.OrderServiceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderServiceImpl orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory", fallbackMethod = "placedOrderFallback")
    public String placeOrder(@RequestBody OrderRequest request){
        log.info("Order placed successfully");
        orderService.placeOrder(request);
        return "Order placed successfully!";
    }

    public String placeOrderFallback(OrderRequest request, RuntimeException e){
        log.error("Exception is: {}", e.getMessage());
        return "FALLBACK INVOKED: Order Placed Failed. Please try again later.";
    }


}