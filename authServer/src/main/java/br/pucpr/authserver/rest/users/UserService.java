package br.pucpr.authserver.rest.users;

import br.pucpr.authserver.lib.exception.ForbiddenException;
import br.pucpr.authserver.lib.exception.NotFoundException;
import br.pucpr.authserver.lib.security.JWT;
import br.pucpr.authserver.rest.users.requests.CreateUserRequest;
import br.pucpr.authserver.rest.users.requests.LoginRequest;
import br.pucpr.authserver.rest.users.requests.UpdateUserRequest;
import br.pucpr.authserver.rest.users.responses.UserCreateResponse;
import br.pucpr.authserver.rest.users.responses.UserGetResponse;
import br.pucpr.authserver.rest.users.responses.UserLoginDTO;
import br.pucpr.authserver.rest.users.responses.UserLoginResponse;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
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
        if(repository.getUserByEmail(request.getEmail()) != null){
            throw new ForbiddenException("This email is already registered");
        }
        User newUser = new User(request);
        Set<String> aux = new HashSet<>();
        aux.add("USER");
        newUser.setRoles(aux);
        return new UserCreateResponse(repository.save(newUser));
    }

    public UserLoginResponse login(LoginRequest credentials){
        var email = credentials.getEmail();
        var password = credentials.getPassword();

        var user = repository.getUserByEmail(email);
        if(user == null) throw new NotFoundException("Wrong email  or password");
        var passwordMatches = new BCryptPasswordEncoder().matches(password, user.getPassword());
        if(!passwordMatches) throw new NotFoundException("Wrong email  or password");

        var token = jwt.createToken(new UserLoginDTO(user));
        return new UserLoginResponse(token, user);
    }

    public void deleteUser(Long id){
        if(!repository.existsById(id)) throw new NotFoundException("User not found");
        repository.deleteById(id);
    }

    public UserGetResponse getUser(Long id){
        if(!repository.existsById(id)) throw new NotFoundException("User not found");
        return new UserGetResponse(repository.findById(id).get());
    }

    public List<UserGetResponse> listAllUsers(){
        var users = repository.findAll(Sort.by(Sort.Order.asc("id")));
        return users.stream().map(u -> new UserGetResponse(u)).toList();
    }


    public UserGetResponse updateUser(Long id, UpdateUserRequest updateRequest) {
        if(!repository.existsById(id)) throw new NotFoundException("User not found");
        var user = repository.findById(id).get();
        user.setUpdatedData(updateRequest);
        return new UserGetResponse(repository.save(user));
    }
}
