package br.pucpr.communityserver.rest.communities.responses;

import br.pucpr.communityserver.rest.communities.Community;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CommunityResponse {

    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private String code;

    @NotEmpty
    private LocalDateTime createdAt;

    public CommunityResponse(Community community){
        this.id = community.getId();
        this.name = community.getName();
        this.description = community.getDescription();
        this.code = community.getCode();
        this.createdAt = community.getCreatedAt();
    }

}
