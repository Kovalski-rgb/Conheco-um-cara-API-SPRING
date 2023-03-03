package br.pucpr.musicserver.rest.users.resonses;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class RoleResponse {

    @NotBlank
    private String name;

}
