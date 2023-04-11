package br.pucpr.communityserver.rest.posts.requests;

import br.pucpr.communityserver.rest.products.Product;
import br.pucpr.communityserver.rest.services.Service;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class EditPostRequest {

    @NotEmpty
    @Schema(example = "Title to be updated")
    private String title;

    @NotEmpty
    @Schema(example = "Description to be updated")
    private String description;

    private Long serviceId;
    private Long productId;
}
