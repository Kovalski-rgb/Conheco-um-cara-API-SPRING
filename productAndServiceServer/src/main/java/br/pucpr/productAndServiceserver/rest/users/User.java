package br.pucpr.productAndServiceserver.rest.users;

import br.pucpr.productAndServiceserver.rest.products.Product;
import br.pucpr.productAndServiceserver.rest.users.requests.UserTokenDTO;
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
    private Long id;

//    TODO change to products and services
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
