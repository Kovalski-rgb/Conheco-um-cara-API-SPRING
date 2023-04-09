package br.pucpr.authserver.rest.users;

import br.pucpr.authserver.rest.users.requests.CreateUserRequest;
import br.pucpr.authserver.rest.users.requests.LoginRequest;
import br.pucpr.authserver.rest.users.responses.UserLoginResponse;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserMock {

    public static CreateUserRequest getUserRequest(){
        return new CreateUserRequest(
            "user",
            "Password#123",
            "email@email.com",
            "1234567890"
        );
    }

    public static LoginRequest getLoginRequest(){
        return new LoginRequest(
            "email@email.com",
            "Password#123"
        );
    }

    public static UserLoginResponse getUserLoginResponse(){
        return new UserLoginResponse(
                "token",
                1L,
                "email@email.com",
                "user",
                Stream.of("USER").collect(Collectors.toSet())
        );
    }

    public static User getUser(CreateUserRequest request){
        var result = new User(request);
        result.setId(1L);
        result.setRoles(Stream.of("USER").collect(Collectors.toSet()));
        return result;
    }

    public static User getUser(){
        var result = new User(getUserRequest());
        result.setId(1L);
        result.setRoles(Stream.of("USER").collect(Collectors.toSet()));
        return result;
    }

}
