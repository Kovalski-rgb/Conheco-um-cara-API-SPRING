package br.pucpr.musicserver.rest.users.resonses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor @AllArgsConstructor
public class UserLoginResponse {
    private String token;
    private Long id;
    private String email;
    private String name;
    private Set<RoleResponse> roles;
}
