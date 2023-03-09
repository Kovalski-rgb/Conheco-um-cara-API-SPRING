package br.pucpr.productAndServiceserver.rest.services.response;

import br.pucpr.productAndServiceserver.rest.services.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private Set<String> productType;

    private LocalDateTime createdAt;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.productType = product.getProductType();
        this.createdAt = product.getCreatedAt();
    }
}
