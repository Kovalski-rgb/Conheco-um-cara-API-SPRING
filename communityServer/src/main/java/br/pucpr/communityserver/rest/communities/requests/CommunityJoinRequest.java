package br.pucpr.communityserver.rest.communities.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CommunityJoinRequest {

    @NotEmpty
    @Schema(example = "Community name")
    private String name;

    @NotEmpty
    @Schema(example = "1234abcd", description = "All codes have a length of 8")
    private String code;
}
