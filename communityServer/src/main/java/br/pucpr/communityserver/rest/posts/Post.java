package br.pucpr.communityserver.rest.posts;

import br.pucpr.communityserver.rest.communities.Community;
import br.pucpr.communityserver.rest.posts.requests.PostRequest;
import br.pucpr.communityserver.rest.products.Product;
import br.pucpr.communityserver.rest.services.Service;
import br.pucpr.communityserver.rest.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NamedQuery(
        name = "Post.getAllPostsFromUserById",
// TODO proper fix to "AND comm.id = comm.id" (thats ugly)
        query = "SELECT p from Post p" +
                " JOIN p.creator c" +
                " JOIN p.community comm" +
                " WHERE c.id = :id" +
                " AND comm.id = comm.id"
)
@NamedQuery(
        name="Post.getAllPostsFromUserByCommunityById",
        query = "SELECT p from Post p" +
                " JOIN p.community comm" +
                " JOIN p.creator creator" +
                " WHERE comm.id = :communityId" +
                " AND creator.id = :userId"
)
@NamedQuery(
        name = "Post.getAllPostsFromCommunityById",
        query = "SELECT p from Post p" +
                " JOIN p.community c" +
                " WHERE c.id = :communityId"
)
@NamedQuery(
        name = "Post.deletePostById",
        query = "DELETE FROM Post p" +
                " WHERE p.id = :postId"
)

@Data @Entity
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "userPosts",
            joinColumns = @JoinColumn(name="post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private User creator;
    @NotNull
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotNull
    @ManyToOne
    @JoinTable(
            name = "communityPosts",
            joinColumns = @JoinColumn(name="post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "community_id", referencedColumnName = "id")
    )
    private Community community;

    @ManyToOne
    private Service service;

    @ManyToOne
    private Product product;

    public Post(PostRequest request) {
        this.title = request.getTitle();
        this.description = request.getDescription();
    }

}
