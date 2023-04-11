package br.pucpr.communityserver.rest.communities.responses;

import br.pucpr.communityserver.rest.communities.Community;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class MultipleCommunitiesResponse {

    @Schema(example = "1")
    private Long id;

    @NotEmpty
    @Schema(example = "Community name response")
    private String name;

    @NotEmpty
    @Schema(example = "Community description response")
    private String description;

    @NotEmpty
    @Schema(example = "2023-04-09 15:53:23.000")
    private LocalDateTime createdAt;

    public MultipleCommunitiesResponse(Community community){
        this.id = community.getId();
        this.name = community.getName();
        this.description = community.getDescription();
        this.createdAt = community.getCreatedAt();
    }

}
