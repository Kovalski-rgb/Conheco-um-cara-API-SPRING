package br.pucpr.communityserver.rest.moderators;

import br.pucpr.communityserver.rest.communities.Community;
import br.pucpr.communityserver.rest.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data @Entity
@AllArgsConstructor @NoArgsConstructor
public class Moderator {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "moderatorUsers",
            joinColumns = @JoinColumn(name = "moderator_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName = "id")
    )
    private Set<User> user;

    @NotNull
    @ManyToOne
    @JoinColumn(name="community_id", nullable=false)
    private Community community;
}
