package ca.gbc.orderservice.service;

import ca.gbc.orderservice.dto.OrderRequest;

public interface OrderService {
    //changed from void to string, to work with timeout in controller
    String placeOrder(OrderRequest orderRequest);
}
