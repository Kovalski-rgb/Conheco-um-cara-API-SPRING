package br.pucpr.authserver.rest.users.responses;

import br.pucpr.authserver.rest.users.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor @AllArgsConstructor
public class UserLoginResponse {

    @Schema(example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...")
    private String token;
    @Schema(example = "1")
    private Long id;
    @Schema(example = "email@email.com")
    private String email;
    @Schema(example = "user name")
    private String name;
    @Schema(example = "USER")
    private Set<String> roles;

    public UserLoginResponse(String token, User user) {
        this.token = token;
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.roles = user.getRoles();
    }
}
