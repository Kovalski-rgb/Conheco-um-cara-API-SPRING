package br.pucpr.productAndServiceserver.rest.products.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data @AllArgsConstructor @NoArgsConstructor
public class UpdateProductRequestDTO {

    @Schema(example = "Updated product name")
    private String name;
    @Schema(example = "Updated product description")
    private String description;
    @Schema(example = "23.45")
    private Double price;
    @Schema(example = "Electronics")
    private Set<String> productType;

}
