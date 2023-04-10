package br.pucpr.communityserver.rest.communities.responses;

import br.pucpr.communityserver.rest.communities.Community;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class MultipleCommunitiesResponse {

    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private LocalDateTime createdAt;

    public MultipleCommunitiesResponse(Community community){
        this.id = community.getId();
        this.name = community.getName();
        this.description = community.getDescription();
        this.createdAt = community.getCreatedAt();
    }

}
