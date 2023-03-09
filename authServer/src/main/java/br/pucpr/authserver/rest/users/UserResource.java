package br.pucpr.authserver.rest.users;

import br.pucpr.authserver.lib.security.JWT;
import br.pucpr.authserver.rest.users.requests.CreateUserRequest;
import br.pucpr.authserver.rest.users.requests.LoginRequest;
import br.pucpr.authserver.rest.users.requests.TestUserRequest;
import br.pucpr.authserver.rest.users.requests.UpdateUserRequest;
import br.pucpr.authserver.rest.users.responses.UserCreateResponse;
import br.pucpr.authserver.rest.users.responses.UserGetResponse;
import br.pucpr.authserver.rest.users.responses.UserLoginResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//TODO add a way to toggle the ADMIN role on registered users

@RestController
@RequestMapping("/users")
public class UserResource {

    private UserService service;
    private JWT jwt;

    public UserResource(UserService service, JWT jwt) {
        this.service = service;
        this.jwt = jwt;
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
    @Transactional
    public ResponseEntity<UserLoginResponse> login(
            @Valid @RequestBody LoginRequest credentials
    ){
        var user = service.login(credentials);
        return user == null ?
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() :
                ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<UserCreateResponse> create(
            @Valid @RequestBody CreateUserRequest credentials
    ){
        var user = service.createUser(credentials);

        return user == null ?
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() :
                ResponseEntity.ok(user);
    }

    @GetMapping("{id}")
    @Transactional
    @SecurityRequirement(name="AuthServer")
    @RolesAllowed({"ADMIN"})
    public UserGetResponse getUser(
            @Valid @RequestParam Long id
    ){
        return service.getUser(id);
    }

    @DeleteMapping("{id}")
    @Transactional
    @SecurityRequirement(name="AuthServer")
    @RolesAllowed({"ADMIN"})
    public void deleteUser(
            @Valid @RequestParam Long id
    ){
        service.deleteUser(id);
    }

    @GetMapping("/me")
    @Transactional
    @SecurityRequirement(name="AuthServer")
    @RolesAllowed({"USER"})
    public ResponseEntity<UserGetResponse> getLoggedUser(
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        var response = service.getUser(jwt.decode(token).getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me")
    @Transactional
    @SecurityRequirement(name="AuthServer")
    @RolesAllowed({"USER"})
    public void deleteLoggedUser(
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        service.deleteUser(jwt.decode(token).getId());
    }

    @PutMapping("/me")
    @Transactional
    @SecurityRequirement(name="AuthServer")
    @RolesAllowed({"USER"})
    public ResponseEntity<UserGetResponse> updateLoggedUser(
            HttpServletRequest request,
            @Valid @RequestBody UpdateUserRequest updateRequest
            ) {
        String token = request.getHeader("Authorization");
        var response = service.updateUser(jwt.decode(token).getId(), updateRequest);
        return response == null ?
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build() :
                ResponseEntity.ok(response);
    }

}
