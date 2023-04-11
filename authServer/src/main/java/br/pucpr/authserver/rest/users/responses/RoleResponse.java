package br.pucpr.authserver.rest.users.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class RoleResponse {

    @NotBlank
    @Schema(example = "user name")
    private String name;

}
