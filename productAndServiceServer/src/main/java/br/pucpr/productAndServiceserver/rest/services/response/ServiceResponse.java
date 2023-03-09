package br.pucpr.productAndServiceserver.rest.services.response;

import br.pucpr.productAndServiceserver.rest.services.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private Set<String> serviceType;

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
