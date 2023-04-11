package br.pucpr.communityserver.rest.communities.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class GetModeratorResponse {

    @Schema(example = "1")
    private Long id;

}
