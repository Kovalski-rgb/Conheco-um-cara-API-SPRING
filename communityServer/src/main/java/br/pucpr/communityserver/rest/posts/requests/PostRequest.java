package br.pucpr.communityserver.rest.posts.requests;

import br.pucpr.communityserver.rest.products.Product;
import br.pucpr.communityserver.rest.services.Service;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @AllArgsConstructor @NoArgsConstructor
public class PostRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotNull
    private Long communityId;

    private Long serviceId;
    private Long productId;

}
