package br.pucpr.authserver.rest.users.responses;

import br.pucpr.authserver.rest.users.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGetResponse {
    private Long id;
    private String name;
    private String email;
    private String telephone;

    public UserGetResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.telephone = user.getTelephone();
    }
}
