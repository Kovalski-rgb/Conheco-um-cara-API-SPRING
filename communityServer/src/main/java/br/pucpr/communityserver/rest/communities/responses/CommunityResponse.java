package br.pucpr.communityserver.rest.communities.responses;

import br.pucpr.communityserver.rest.communities.Community;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @AllArgsConstructor
public class CommunityResponse {


    @Schema(example = "1")
    private Long id;

    @NotEmpty
    @Schema(example = "Community name")
    private String name;

    @NotEmpty
    @Schema(example = "Community description")
    private String description;

    @NotEmpty
    @Schema(example = "12334abcd")
    private String code;

    @NotEmpty
    @Schema(example = "2023-04-09 15:53:23.000")
    private LocalDateTime createdAt;

    public CommunityResponse(Community community){
        this.id = community.getId();
        this.name = community.getName();
        this.description = community.getDescription();
        this.code = community.getCode();
        this.createdAt = community.getCreatedAt();
    }

}
