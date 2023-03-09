package br.pucpr.productAndServiceserver.rest.users;

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
//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "usersCommunities",
//            joinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "community_id", referencedColumnName = "id")
//    )
//    private Set<Community> communities;

    public User(UserTokenDTO userTokenDTO) {
        this.id = userTokenDTO.getId();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
//                ", communities=" + communities +
                '}';
    }
}
