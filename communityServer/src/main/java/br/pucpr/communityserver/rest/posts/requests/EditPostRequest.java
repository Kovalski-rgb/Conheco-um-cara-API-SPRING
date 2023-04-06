package br.pucpr.communityserver.rest.posts.requests;

import br.pucpr.communityserver.rest.products.Product;
import br.pucpr.communityserver.rest.services.Service;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class EditPostRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    private Long serviceId;
    private Long productId;
}
