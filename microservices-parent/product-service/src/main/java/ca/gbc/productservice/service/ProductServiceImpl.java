package ca.gbc.productservice.service;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.model.Product;
import ca.gbc.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service //we dont expose the interface, we expose the Impl (implementation)
@RequiredArgsConstructor
@Slf4j //logger
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public void createProduct(ProductRequest productRequest) {

        log.debug("Creating a new product {}", productRequest.getName());

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

    }

    @Override
    public void updateProduct(String productId, ProductRequest productRequest) {

    }

    @Override
    public void deleteProduct(String productId) {

    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return null;
    }
}
