package br.pucpr.productAndServiceserver.rest.users;

import br.pucpr.productAndServiceserver.rest.users.requests.UserTokenDTO;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserMock {

    public static User getUser(){
        return new User(
                1L,
                new HashSet<>(),
                new HashSet<>()
        );
    }

    public static UserTokenDTO getUserTokenDTO(){
        return new UserTokenDTO(
                1L,
                "email@email.com",
                "name",
                Stream.of("USER").collect(Collectors.toSet())
        );
    }

}
