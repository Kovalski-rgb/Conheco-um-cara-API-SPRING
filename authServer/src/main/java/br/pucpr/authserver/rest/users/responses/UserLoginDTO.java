package br.pucpr.authserver.rest.users.responses;

import br.pucpr.authserver.rest.users.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


/**
 * Use to store user data that will be converted into a token
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO {
        private Long id;
        private String email;
        private String name;
        private Set<String> roles;

        public UserLoginDTO(User user) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.name = user.getName();
            this.roles = user.getRoles();
        }

}
