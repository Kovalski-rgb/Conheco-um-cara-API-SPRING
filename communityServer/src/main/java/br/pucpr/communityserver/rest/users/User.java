package br.pucpr.communityserver.rest.users;

import br.pucpr.communityserver.rest.communities.Community;
import br.pucpr.communityserver.rest.users.requests.UserTokenDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "usersCommunities",
            joinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "community_id", referencedColumnName = "id")
    )
    private Set<Community> communities;

    public User(UserTokenDTO userTokenDTO) {
        this.id = userTokenDTO.getId();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", communities=" + communities +
                '}';
    }
}
