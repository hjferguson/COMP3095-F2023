package ca.gbc.productservice.service;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.model.Product;
import ca.gbc.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ExecutableFindOperation;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.MongoTemplate; //this was the most similar in name



import org.springframework.stereotype.Service;

import java.util.List;


@Service //we dont expose the interface, we expose the Impl (implementation)
@RequiredArgsConstructor
@Slf4j //logger
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public void createProduct(ProductRequest productRequest) {

        log.info("Creating a new product {}", productRequest.getName());

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        //make the object but then we need to persist (store) it somewhere

        productRepository.save(product);

        log.info("Product {} is saved", product.getId());

    }

    @Override
    public String updateProduct(String productId, ProductRequest productRequest) {

        log.info("Updating a product with id {}", productId); //good practice to log to logfile. se what methods got invoked

        Query query = new Query(); //mongo.core
        query.addCriteria(Criteria.where("Id").is(productId)); //Sergio has "id" instead of "Id"
        Product product = mongoTemplate.findOne(query, Product.class); //ask Sergio about this

        if(product != null){
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());


            log.info("Product {} is updated", product.getId());
            return productRepository.save(product).getId();
        }


        return productId;


    }

    @Override
    public void deleteProduct(String productId) {

        log.info("Product {} is deleted", productId);
        productRepository.deleteById(productId);
    }

    @Override
    public List<ProductResponse> getAllProducts() {

        log.info("Returning a list of products:");

        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {

        return ProductResponse.builder()
                .Id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }



}
