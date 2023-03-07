package br.pucpr.authserver.rest.users;

import br.pucpr.authserver.lib.security.JWT;
import br.pucpr.authserver.rest.users.requests.CreateUserRequest;
import br.pucpr.authserver.rest.users.requests.LoginRequest;
import br.pucpr.authserver.rest.users.responses.UserCreateResponse;
import br.pucpr.authserver.rest.users.responses.UserLoginResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private JWT jwt;
    private UserRepository repository;

    public UserService(JWT jwt, UserRepository repository) {
        this.jwt = jwt;
        this.repository = repository;
    }

    public UserLoginResponse createTestUser(String token){
        return jwt.createTestUser(token);
    }

    public UserCreateResponse createUser(CreateUserRequest request){
        User newUser = new User(request);
        Set<String> aux = new HashSet<>();
        aux.add("USER");
        newUser.setRoles(aux);
        return new UserCreateResponse(repository.save(newUser));
    }

    public UserLoginResponse login(LoginRequest credentials){
        credentials.setPassword(new BCryptPasswordEncoder().encode(credentials.getPassword()));
        var email = credentials.getEmail();
        var password = credentials.getPassword();
        var user = repository.getUserByEmailPassword(email, password);
        var token = jwt.createtoken(user);
        return new UserLoginResponse(token, user);
    }

}
