package br.pucpr.authserver.rest.users.responses;

import br.pucpr.authserver.rest.users.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateResponse {
    @NotEmpty
    private String name;

    @NotEmpty
    private String email;

    private String telephone;

    public UserCreateResponse(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.telephone = user.getTelephone();
    }
}
