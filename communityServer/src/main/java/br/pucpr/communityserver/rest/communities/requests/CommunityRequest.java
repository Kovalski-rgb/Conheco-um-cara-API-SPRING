package br.pucpr.communityserver.rest.communities.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommunityRequest {
    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

}
