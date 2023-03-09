package br.pucpr.productAndServiceserver.rest.products.request;

import br.pucpr.productAndServiceserver.rest.users.User;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank;
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
public class ProductRegisterRequest {

    @NotEmpty
    private String name;

    private String description;

    @NotNull
    private Double price;

    @NotEmpty
    private Set<String> productType;

}
