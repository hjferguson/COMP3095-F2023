package ca.gbc.orderservice.controller;

import ca.gbc.orderservice.dto.OrderRequest;
import ca.gbc.orderservice.service.OrderServiceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    //commented this out, to add timer to the controller. that's configed in app prop

//    private final OrderServiceImpl orderService;
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    @CircuitBreaker(name="inventory", fallbackMethod = "placedOrderFallback")
//    public String placeOrder(@RequestBody OrderRequest request){
//        log.info("Order placed successfully");
//        orderService.placeOrder(request);
//        return "Order placed successfully!";
//    }
//
//    public String placeOrderFallback(OrderRequest request, RuntimeException e){
//        log.error("Exception is: {}", e.getMessage());
//        return "FALLBACK INVOKED: Order Placed Failed. Please try again later.";
//    }

    private final OrderServiceImpl orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory", fallbackMethod = "placeOrderFallback")
    @TimeLimiter(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest request){
        log.info("Order placed successfully");
        orderService.placeOrder(request);
        //return "Order placed successfully!";
        return CompletableFuture.supplyAsync(() -> orderService.placeOrder(request));
    }


    public CompletableFuture<String> placeOrderFallback(OrderRequest request, RuntimeException e){
        log.error("Exception is: {}", e.getMessage());
        return CompletableFuture.supplyAsync(() -> "FALLBACK INVOKED: Order Placed Failed. Please try again later");
    }


}