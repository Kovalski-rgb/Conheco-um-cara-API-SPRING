package br.pucpr.communityserver.rest.communities.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CommunityRequest {

    @NotEmpty
    @Schema(example = "Community name")
    private String name;

    @NotEmpty
    @Schema(example = "Community description")
    private String description;

}
