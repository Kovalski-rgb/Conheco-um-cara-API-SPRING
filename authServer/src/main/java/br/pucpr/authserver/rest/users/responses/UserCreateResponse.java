package br.pucpr.authserver.rest.users.responses;

import br.pucpr.authserver.rest.users.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateResponse {
    @NotEmpty
    @Schema(example = "user name")
    private String name;

    @NotEmpty
    @Schema(example = "email@email.com")
    private String email;

    @Schema(example = "12345-6789")
    private String telephone;

    public UserCreateResponse(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.telephone = user.getTelephone();
    }

    @Override
    public String toString() {
        return "UserCreateResponse{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
