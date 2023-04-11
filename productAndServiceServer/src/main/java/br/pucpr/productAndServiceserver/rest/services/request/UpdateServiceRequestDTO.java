package br.pucpr.productAndServiceserver.rest.services.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data @AllArgsConstructor @NoArgsConstructor
public class UpdateServiceRequestDTO {

    @Schema(example = "Updated service name")
    private String name;
    @Schema(example = "Updated service description")
    private String description;
    @Schema(example = "234.56")
    private Double price;
//    @Schema(example = "HouseKeeping")
//    private Set<String> serviceType;

}
