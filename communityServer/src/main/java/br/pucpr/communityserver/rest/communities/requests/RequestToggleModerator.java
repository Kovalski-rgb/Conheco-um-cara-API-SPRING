package br.pucpr.communityserver.rest.communities.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class RequestToggleModerator {

    @NotNull
    private Long communityId;

    @NotNull
    private Long userId;

}
