package br.pucpr.communityserver.rest.communities;

import br.pucpr.communityserver.rest.communities.requests.CommunityRequest;
import br.pucpr.communityserver.rest.communities.requests.CommunityJoinRequest;
import br.pucpr.communityserver.rest.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data @Entity
@AllArgsConstructor
@NoArgsConstructor

@NamedQuery(
        name="Community.getCommunitiesByCodeAndName",
        query = "SELECT c FROM Community c " +
                "WHERE c.code = :code " +
                "and c.name = :name"
)
@NamedQuery(
        name="Community.getCommunitiesByCode",
        query = "SELECT code FROM Community c " +
                "WHERE code = :code"
)

public class Community {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String name;

    private String description;

    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "communities")
    private Set<User> users;

    @NotEmpty
    private String code;

    public Community(CommunityRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
    }

}
