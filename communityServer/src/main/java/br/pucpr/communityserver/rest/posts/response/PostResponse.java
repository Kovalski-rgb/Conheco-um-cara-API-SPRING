package br.pucpr.communityserver.rest.posts.response;

import br.pucpr.communityserver.rest.posts.Post;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "1")
    private Long id;

    @NotEmpty
    @Schema(example = "Response post title")
    private String title;

    @NotEmpty
    @Schema(example = "Response post description")
    private String description;

    @NotNull
    @Schema(example = "1")
    private Long creatorId;

    @NotNull
    @Schema(example = "2023-04-09 15:53:23.000")
    private LocalDateTime createdAt;

    @NotNull
    @Schema(example = "1")
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
