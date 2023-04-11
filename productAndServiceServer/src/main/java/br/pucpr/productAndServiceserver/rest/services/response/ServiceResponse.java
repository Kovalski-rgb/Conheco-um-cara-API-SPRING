package br.pucpr.productAndServiceserver.rest.services.response;

import br.pucpr.productAndServiceserver.rest.services.Service;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Service name")
    private String name;

    @Schema(example = "Service description")
    private String description;

    @Schema(example = "123.45")
    private Double price;

    @Schema(example = "Cleaning")
    private Set<String> serviceType;

    @Schema(example = "2023-04-09 15:53:23.000")
    private LocalDateTime createdAt;

    public ServiceResponse(Service service) {
        this.id = service.getId();
        this.name = service.getName();
        this.description = service.getDescription();
        this.price = service.getPrice();
        this.serviceType = service.getServiceType();
        this.createdAt = service.getCreatedAt();
    }
}
