package br.pucpr.authserver.rest.users.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    @NotEmpty
    @Schema(example = "User name")
    private String name;

    @NotEmpty
    @Pattern(regexp = "^(?=(.*[a-z])+)(?=(.*[A-Z])+)(?=(.*[0-9])+)(?=(.*[!@#$%^&*()\\-_+.])+).{8,}$")
    @Schema(example = "String#01")
    private String password;

    @NotEmpty
    @Pattern(regexp = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")
    @Schema(example = "email@email.com")
    private String email;

    @Pattern(regexp = "\\(?([0-9]{5})\\)?([ -]?)([0-9]{4})")
    @Schema(example = "12345-6789")
    private String telephone;
}
