package br.pucpr.authserver.rest.users;

import br.pucpr.authserver.rest.users.requests.CreateUserRequest;
import br.pucpr.authserver.rest.users.requests.UpdateUserRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
    @Schema(example = "1")
    private Long id;

    @NotEmpty
    @Schema(example = "User name")
    private String name;

    @NotEmpty
    @Schema(example = "email@email.com")
    private String email;

    @NotEmpty
    @Schema(example = "S3Cre7_Password")
    private String password;

    @Schema(example = "12345-6789")
    private String telephone;

    @Schema(example = "USER")
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

    public void setUpdatedData(UpdateUserRequest newUser){
        if(newUser.getName() != null && !newUser.getName().isEmpty()) this.setName(newUser.getName());
        if(newUser.getEmail() != null && !newUser.getEmail().isEmpty()) this.setEmail(newUser.getEmail());
        if(newUser.getTelephone() != null && !newUser.getTelephone().isEmpty()) this.setTelephone(newUser.getTelephone());
        if(newUser.getPassword() != null && !newUser.getPassword().isEmpty()) this.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", telephone='" + telephone + '\'' +
                ", roles=" + roles +
                '}';
    }

}
