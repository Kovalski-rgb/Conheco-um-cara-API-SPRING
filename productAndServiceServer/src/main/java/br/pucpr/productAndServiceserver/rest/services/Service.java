package br.pucpr.productAndServiceserver.rest.services;

import br.pucpr.productAndServiceserver.rest.services.request.ServiceRequest;
import br.pucpr.productAndServiceserver.rest.services.request.UpdateServiceRequestDTO;
import br.pucpr.productAndServiceserver.rest.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;


@NamedQuery(
        name="Service.getServicesByOwnerId",
        query = "SELECT s from Service s" +
                " JOIN s.owner o" +
                " WHERE o.id = :ownerId"
)
@NamedQuery(
        name="Service.countAllServices",
        query = "SELECT COUNT(s) FROM Service s"
)
@NamedQuery(
        name="Service.countServicesByOwnerId",
        query = "SELECT COUNT(s) FROM Service s" +
                " JOIN s.owner o" +
                " WHERE o.id = :ownerId"
)
@NamedQuery(
        name="Service.selectAllServices",
        query = "SELECT s from Service s"
)

@Entity @Data
@AllArgsConstructor
@NoArgsConstructor
public class Service {
    @Id @GeneratedValue
    private Long id;

    @NotEmpty
    private String name;

    private String description;

    @NotNull
    @ManyToOne
    private User owner;

    @NotNull
    private Double price;

    @NotEmpty
    @ElementCollection
    @CollectionTable
            (name = "ServiceType", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "serviceType")
    private Set<String> serviceType;

    private LocalDateTime createdAt;

    public Service fromRequest(ServiceRequest request){
        this.name = request.getName();
        this.description = request.getDescription();
        this.serviceType = request.getServiceType();
        this.price = request.getPrice();
        return this;
    }

    public void update(UpdateServiceRequestDTO service){
        if(service.getName() != null && !service.getName().isEmpty()) this.name = service.getName();
        if(service.getDescription() != null) this.description = service.getDescription();
        if(service.getPrice() != null  && !service.getPrice().isNaN())this.price = service.getPrice();
        if(service.getServiceType() != null) this.serviceType = service.getServiceType();
    }

}
