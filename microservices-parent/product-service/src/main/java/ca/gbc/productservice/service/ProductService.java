package ca.gbc.productservice.service;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    void createProduct(ProductRequest productRequest);

    void updateProduct(String productId, ProductRequest productRequest);

    void deleteProduct(String productId);

    List<ProductResponse> getAllProducts();

}
