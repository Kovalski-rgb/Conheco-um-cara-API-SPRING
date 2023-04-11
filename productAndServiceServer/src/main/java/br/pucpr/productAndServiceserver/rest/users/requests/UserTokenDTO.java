package br.pucpr.productAndServiceserver.rest.users.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenDTO {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "email@email.com")
    private String email;
    @Schema(example = "user name")
    private String name;
    @Schema(example = "USER")
    private Set<String> roles;
}
