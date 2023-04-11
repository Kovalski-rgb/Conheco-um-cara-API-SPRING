package br.pucpr.productAndServiceserver.rest.products.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotNull
    @Schema(example = "Product name")
    private String name;

    @NotNull
    @Schema(example = "Product description")
    private String description;

    @NotNull
    @Schema(example = "12.34")
    private Double price;
//
//    @NotNull
//    @Schema(example = "Headphones")
//    private Set<String> productType;

}
