package br.pucpr.communityserver.rest.users;

import br.pucpr.communityserver.rest.users.requests.LoginRequest;
import br.pucpr.communityserver.rest.users.requests.TestUserRequest;
import br.pucpr.communityserver.rest.users.responses.UserLoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserResource {

    private UserService service;

    public UserResource(UserService service) {
        this.service = service;
    }

}
