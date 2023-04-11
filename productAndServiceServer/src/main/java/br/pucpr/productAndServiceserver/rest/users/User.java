package br.pucpr.productAndServiceserver.rest.users;

import br.pucpr.productAndServiceserver.rest.products.Product;
import br.pucpr.productAndServiceserver.rest.services.Service;
import br.pucpr.productAndServiceserver.rest.users.requests.UserTokenDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data @Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Schema(example = "1")
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "owner", cascade = { CascadeType.PERSIST })
    private Set<Service> services;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "owner", cascade = { CascadeType.PERSIST })
    private Set<Product> products;

    public User(UserTokenDTO userTokenDTO) {
        this.id = userTokenDTO.getId();
    }

    public User fromUserTokenDTO(UserTokenDTO tokenDTO){
        this.id = tokenDTO.getId();
        return this;
    }

    @Override
    public String toString() {
        return "User{id=" + id + '}';
    }
}
