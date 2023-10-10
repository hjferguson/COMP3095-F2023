package ca.gbc.productservice.controller;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController //returns JSON or XML. @Controller returns HTML
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    //controllers are intimate with services
    private final ProductServiceImpl productService;

    //this exposes an endpoint for POST requests.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) //always return status codes back to client. 201.
    public void createProduct(@RequestBody ProductRequest productRequest){
        productService.createProduct(productRequest);
    }

    //Reading / GET requests
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse>getAllProducts(){
        return productService.getAllProducts();
    }

    //UPDATE / PUT
    @PutMapping({"/{productId}"})
    public ResponseEntity<?> updateProduct(@PathVariable("productId") String productId,
                                           @RequestBody ProductRequest productRequest) {
        String updatedProductId = productService.updateProduct(productId, productRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/products/" + updatedProductId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT); //no content because we are just updating
    }

    @DeleteMapping({"/{productId}"})
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") String productId){
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
