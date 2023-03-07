package br.pucpr.authserver.rest.users.responses;

import br.pucpr.authserver.rest.users.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor @AllArgsConstructor
public class UserLoginResponse {
    private String token;
    private Long id;
    private String email;
    private String name;
    private Set<RoleResponse> roles;

    public UserLoginResponse(String token, User user) {
        this.token = token;
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        Set<String> roles = user.getRoles();
        Set<RoleResponse> roleSet = new HashSet<>();
        for(String role : roles){
            roleSet.add(new RoleResponse((role)));
        }
        this.roles = roleSet;
    }
}
