package br.pucpr.communityserver.rest.communities;

import br.pucpr.communityserver.rest.communities.requests.CommunityRequest;
import br.pucpr.communityserver.rest.communities.requests.CommunityJoinRequest;
import br.pucpr.communityserver.rest.posts.Post;
import br.pucpr.communityserver.rest.users.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data @Entity
@AllArgsConstructor
@NoArgsConstructor

@NamedQuery(
        name = "Community.getCodesFromAllCommunitiesByName",
        query = "SELECT c.code FROM Community c" +
                " WHERE c.name = :name"
)
@NamedQuery(
        name="Community.getCommunitiesByCodeAndName",
        query = "SELECT c FROM Community c" +
                " WHERE c.code = :code" +
                " AND c.name = :name"
)
@NamedQuery(
        name="Community.getCommunityById",
        query = "SELECT c FROM Community c" +
                " LEFT JOIN FETCH c.users" +
                " LEFT JOIN FETCH c.moderators" +
                " WHERE c.id = :id"
)
@NamedQuery(
        name = "Community.getUserInCommunityById",
        query = "SELECT users FROM Community c" +
                " JOIN c.users u"+
                " WHERE c.id = :communityId" +
                " AND u.id = :userId"
)
@NamedQuery(
        name = "Community.getAllCommunitiesByUserId",
        query = "SELECT c FROM Community c" +
                " JOIN c.users u" +
                " WHERE u.id = :userId"
)
@NamedQuery(
        name = "Community.getAllModeratorsByCommunityId",
        query = "SELECT moderators FROM Community c" +
                " WHERE c.id = :communityId"
)
@NamedQuery(
        name = "Community.getModeratorByCommunityAndUser",
        query = "SELECT moderators FROM Community c" +
                " JOIN c.moderators m" +
                " WHERE c.id = :communityId" +
                " AND m.id = :userId"
)
@NamedQuery(
        name = "Community.getModeratorListFromCommunityById",
        query = "SELECT moderators FROM Community c" +
                " WHERE c.id = :id"
)
@NamedQuery(
        name = "Community.getUserListFromCommunityById",
        query = "SELECT users FROM Community c" +
                " WHERE c.id = :id"
)
@NamedQuery(
        name = "Community.deleteCommunityById",
        query = "DELETE FROM Community c" +
                " WHERE c.id = :id"
)
//@NamedQuery(
//        name = "Community.updateCommunityById",
//        query = "UPDATE Community c" +
//                " SET c = :community" +
//                " WHERE c.id = :id"
//)
public class Community {

    public static int CODE_LENGTH = 8;

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String name;

    private String description;

    private LocalDateTime createdAt;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "usersCommunities",
            joinColumns = @JoinColumn(name="community_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private Set<User> users;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "communityModerators",
            joinColumns = @JoinColumn(name="community_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private Set<User> moderators;

    //TODO check why [cascade = CascadeType.ALL, orphanRemoval = true] does not guarantee that deleting a community
    // will also delete all created posts, fix this
    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @NotEmpty
    private String code;

    public Community(CommunityRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
    }

    @Override
    public String toString() {
        return "Community{" +
                "id=" + id;
    }
}
