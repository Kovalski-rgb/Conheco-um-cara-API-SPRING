package br.pucpr.authserver.rest.users;

import br.pucpr.authserver.rest.users.requests.CreateUserRequest;
import br.pucpr.authserver.rest.users.requests.LoginRequest;
import br.pucpr.authserver.rest.users.requests.TestUserRequest;
import br.pucpr.authserver.rest.users.responses.UserCreateResponse;
import br.pucpr.authserver.rest.users.responses.UserLoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserResource {

    private UserService service;

    public UserResource(UserService service) {
        this.service = service;
    }

    @PostMapping("/testUser")
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

    @PostMapping("/create")
    public ResponseEntity<UserCreateResponse> create(
            @Valid @RequestBody CreateUserRequest credentials
    ){
        var user = service.createUser(credentials);
//        var result = Map.of("user", user);
//
//        for(Map.Entry<String, UserCreateResponse> entry : result.entrySet()){
//            System.out.println("key " + entry.getKey());
//            System.out.println("value " + entry.getValue());
//        }

        return user == null ?
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() :
                ResponseEntity.ok(user);
    }
}
