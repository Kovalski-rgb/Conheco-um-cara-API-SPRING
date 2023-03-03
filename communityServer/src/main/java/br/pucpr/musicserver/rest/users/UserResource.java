package br.pucpr.musicserver.rest.users;

import br.pucpr.musicserver.lib.security.JWT;
import br.pucpr.musicserver.rest.users.requests.LoginRequest;
import br.pucpr.musicserver.rest.users.requests.TestUserRequest;
import br.pucpr.musicserver.rest.users.resonses.UserLoginResponse;
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

    @PostMapping("/test")
    public ResponseEntity<UserLoginResponse> getTestUser(
            @Valid @RequestBody TestUserRequest credentials
    ){
        var user = service.createTestUser(credentials.getToken());
        return user == null ?
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() :
            ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(
            @Valid @RequestBody LoginRequest credentials
    ){
        var user = service.login(credentials);
        return user == null ?
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() :
                ResponseEntity.ok(user);
    }
}
