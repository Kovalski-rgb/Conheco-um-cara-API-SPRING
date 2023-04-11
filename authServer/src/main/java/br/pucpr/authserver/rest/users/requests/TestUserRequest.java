package br.pucpr.authserver.rest.users.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestUserRequest {
    @Schema(example = "secret token")
    private String token;
}
