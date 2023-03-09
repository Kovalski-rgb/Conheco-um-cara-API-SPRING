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
        query = "SELECT c FROM Community c" +
                " WHERE c.code = :code" +
                " AND c.name = :name"
)
@NamedQuery(
        name="Community.getCommunitiesByCode",
        query = "SELECT code FROM Community c" +
                " WHERE code = :code"
)
@NamedQuery(
        name = "Community.getUserInCommunityById",
        query = "SELECT users FROM Community c" +
                " JOIN c.users u"+
                " WHERE c.id = :communityId" +
                " AND u.id = :userId"
)
@NamedQuery(
        name = "Community.removeUserFromCommunity",
        query = "DELETE FROM User u" +
                " WHERE u.id = :userId" +
                " AND u IN" +
                    " (SELECT c.users FROM Community c WHERE c.id = :communityId)"
)
@NamedQuery(
        name = "Community.getAllCommunitiesByUserId",
        query = "SELECT c FROM Community c" +
                " JOIN c.users u" +
                " WHERE u.id = :userId"
)
@NamedQuery(
        name = "Community.insertNewUserIntoCommunity",
        query = "INSERT INTO Community c" +
                " JOIN c.users u" +
                " VALUES (:userId, :communityId)"
)

public class Community {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String name;

    private String description;

    private LocalDateTime createdAt;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "usersCommunities",
            joinColumns = @JoinColumn(name="community_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private Set<User> users;

    @NotEmpty
    private String code;

    public Community(CommunityRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
    }

}
