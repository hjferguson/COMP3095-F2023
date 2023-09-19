package ca.gbc.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

//DTO is a data transfer object. Used to encapsulate data and transfer between different parts of application
//in context of web dev, transfers data between the back and front ends.
public class ProductRequest {

    private String name;
    private String description;
    private BigDecimal price;


}
