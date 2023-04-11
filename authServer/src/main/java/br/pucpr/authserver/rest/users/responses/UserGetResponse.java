package br.pucpr.authserver.rest.users.responses;

import br.pucpr.authserver.rest.users.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGetResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "user name")
    private String name;
    @Schema(example = "email@email.com")
    private String email;

    @Schema(example = "12345-6789")
    private String telephone;

    public UserGetResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.telephone = user.getTelephone();
    }
}
