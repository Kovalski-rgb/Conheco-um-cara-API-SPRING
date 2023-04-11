package br.pucpr.communityserver.rest.communities.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class RequestToggleModerator {

    @NotNull
    @Schema(example = "1")
    private Long communityId;

    @NotNull
    @Schema(example = "1")
    private Long userId;

}
