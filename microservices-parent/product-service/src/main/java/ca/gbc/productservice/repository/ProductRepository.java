package ca.gbc.productservice.repository;

import ca.gbc.productservice.model.Product;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository; //this a dependancy we added when initially making the project

public interface ProductRepository extends MongoRepository<Product, String> {
    @DeleteQuery
    void deleteById(String productId);
}
