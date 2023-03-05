package br.pucpr.communityserver.rest.communities;

import br.pucpr.communityserver.rest.moderators.Moderator;
import br.pucpr.communityserver.rest.posts.Post;
import br.pucpr.communityserver.rest.users.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Community {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String name;

    private String description;

    private LocalDate createdAt;

    private Set<User> users;

    private Set<Moderator> moderators;

    private Set<Post> posts;

    @NotEmpty
    private String code;
}
