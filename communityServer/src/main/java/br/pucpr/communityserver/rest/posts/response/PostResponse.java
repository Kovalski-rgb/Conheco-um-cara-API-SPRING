package br.pucpr.communityserver.rest.posts.response;

import br.pucpr.communityserver.rest.posts.Post;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class PostResponse {

    @NotNull
    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotNull
    private Long creatorId;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private Long communityId;

    private Long serviceId;
    private Long productId;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.creatorId = post.getCreator().getId();
        this.createdAt = post.getCreatedAt();
        this.communityId = post.getCommunity().getId();
        this.serviceId = ( post.getService() != null ) ? post.getService().getId() : null;
        this.productId = ( post.getProduct() != null ) ? post.getProduct().getId() : null;
    }
}
