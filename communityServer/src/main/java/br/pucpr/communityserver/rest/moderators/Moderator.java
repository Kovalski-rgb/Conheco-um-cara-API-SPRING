package br.pucpr.communityserver.rest.moderators;

import br.pucpr.communityserver.rest.communities.Community;
import br.pucpr.communityserver.rest.users.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class Moderator {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private User user;

    @NotNull
    private Community community;
}
