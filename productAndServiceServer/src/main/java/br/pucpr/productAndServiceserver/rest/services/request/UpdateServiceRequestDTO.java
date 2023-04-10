package br.pucpr.productAndServiceserver.rest.services.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data @AllArgsConstructor @NoArgsConstructor
public class UpdateServiceRequestDTO {

    private String name;
    private String description;
    private Double price;
    private Set<String> serviceType;

}
