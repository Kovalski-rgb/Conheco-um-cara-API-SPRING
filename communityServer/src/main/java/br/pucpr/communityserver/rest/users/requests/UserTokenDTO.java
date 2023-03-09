package br.pucpr.communityserver.rest.users.requests;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenDTO {

    private Long id;
    private String email;
    private String name;
    private Set<String> roles;
}
