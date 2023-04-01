package br.pucpr.communityserver.rest.users;

import br.pucpr.communityserver.rest.communities.Community;
import br.pucpr.communityserver.rest.moderators.Moderator;
import br.pucpr.communityserver.rest.users.requests.UserTokenDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

@Data @Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "users")
    private Set<Community> communities;

    public User(UserTokenDTO userTokenDTO) {
        this.id = userTokenDTO.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
