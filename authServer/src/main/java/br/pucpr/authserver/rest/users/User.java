package br.pucpr.authserver.rest.users;

import br.pucpr.authserver.rest.users.requests.CreateUserRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Data @Entity
@AllArgsConstructor
@NoArgsConstructor

@NamedQuery(
        name="User.getUserByEmailPassword",
        query = "SELECT u FROM User u " +
                "JOIN u.roles r " +
                "WHERE u.email = :email and " +
                "u.password = :password"
)

public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    private String telephone;

    @ElementCollection
    @CollectionTable(name="roles", joinColumns = @JoinColumn(name = "id"))
    @Column(name="genre")
    @NotEmpty
    private Set<String> roles;

    public User(CreateUserRequest request) {
        this.name = request.getName();
        this.password = new BCryptPasswordEncoder().encode(request.getPassword());
        this.email = request.getEmail();
        this.telephone = request.getTelephone();
    }

}
