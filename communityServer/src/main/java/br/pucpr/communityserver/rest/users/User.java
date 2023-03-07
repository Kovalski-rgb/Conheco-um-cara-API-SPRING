package br.pucpr.communityserver.rest.users;

import br.pucpr.communityserver.rest.communities.Community;
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
    @GeneratedValue
    private Long id;

    private Set<String> roles;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany
    @Column(name="userCommunities")
    @JoinTable(
            name = "usersCommunities",
            joinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "community_id", referencedColumnName = "id")
    )
    private Set<Community> communities;

}
