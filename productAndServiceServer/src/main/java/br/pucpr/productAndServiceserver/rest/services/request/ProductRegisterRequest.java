package br.pucpr.productAndServiceserver.rest.services.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRegisterRequest {

    @NotEmpty
    private String name;

    private String description;

    @NotNull
    private Double price;

    @NotEmpty
    private Set<String> productType;

}
