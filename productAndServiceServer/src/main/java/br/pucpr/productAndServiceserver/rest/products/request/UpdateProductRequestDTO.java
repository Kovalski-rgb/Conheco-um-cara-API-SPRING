package br.pucpr.productAndServiceserver.rest.products.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data @AllArgsConstructor @NoArgsConstructor
public class UpdateProductRequestDTO {

    private String name;
    private String description;
    private Double price;
    private Set<String> productType;

}
