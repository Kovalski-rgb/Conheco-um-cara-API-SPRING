package br.pucpr.communityserver.rest.communities.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CommunityRequest {
    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

}
