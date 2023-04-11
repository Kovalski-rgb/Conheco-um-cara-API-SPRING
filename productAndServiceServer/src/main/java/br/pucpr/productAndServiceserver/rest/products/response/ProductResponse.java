package br.pucpr.productAndServiceserver.rest.products.response;

import br.pucpr.productAndServiceserver.rest.products.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Product name")
    private String name;

    @Schema(example = "Product description")
    private String description;

    @Schema(example = "12.34")
    private Double price;

    @Schema(example = "Headphones")
    private Set<String> productType;

    @Schema(example = "2023-04-09 15:53:23.000")
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
