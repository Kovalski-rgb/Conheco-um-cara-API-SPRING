package br.pucpr.productAndServiceserver.rest.services.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequest {

    @NotNull
    @Schema(example = "Service name")
    private String name;
    @NotNull
    @Schema(example = "Service description")
    private String description;
    @NotNull
    @Schema(example = "123.45")
    private Double price;
//    @NotNull
//    @Schema(example = "Cleaning")
//    private Set<String> serviceType;

}
