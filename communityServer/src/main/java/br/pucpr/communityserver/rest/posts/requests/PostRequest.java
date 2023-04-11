package br.pucpr.communityserver.rest.posts.requests;

import br.pucpr.communityserver.rest.products.Product;
import br.pucpr.communityserver.rest.services.Service;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @AllArgsConstructor @NoArgsConstructor
public class PostRequest {

    @NotEmpty
    @Schema(example = "Post Title!")
    private String title;

    @NotEmpty
    @Schema(example = "Post Description!")
    private String description;

    @NotNull
    @Schema(example = "1")
    private Long communityId;

    private Long serviceId;
    private Long productId;

}
