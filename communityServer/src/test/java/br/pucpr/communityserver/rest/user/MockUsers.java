package br.pucpr.communityserver.rest.user;

import br.pucpr.communityserver.rest.users.User;
import br.pucpr.communityserver.rest.users.requests.UserTokenDTO;
import br.pucpr.communityserver.rest.users.responses.UserResponse;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MockUsers {

    public static User getUser(){
        return new User(
                1L,
                new HashSet<>()
        );
    }

    public static UserResponse getUserResponse(){
        return new UserResponse(1L);
    }

    public static UserTokenDTO getUserTokenDTO(){
        return new UserTokenDTO(
                1L,
                "email@email.com",
                "name",
                Stream.of("USER").collect(Collectors.toSet())
        );
    }

    public static UserTokenDTO getAdminUserTokenDTO(){
        return new UserTokenDTO(
                1L,
                "email@email.com",
                "name",
                Stream.of("USER", "ADMIN").collect(Collectors.toSet())
        );
    }

}

