package br.pucpr.communityserver.rest.community;

import br.pucpr.communityserver.rest.users.requests.UserTokenDTO;

import java.util.HashSet;
import java.util.Set;

public class UserDtoStub {

    public static UserTokenDTO getAdminUser(){
        return new UserTokenDTO(1L, "admin@email.com", "admin", Set.of("USER", "ADMIN"));
    }

    public static UserTokenDTO getGuestUser1(){
        return new UserTokenDTO(2L, "guest1@email.com", "admin", Set.of("USER"));
    }

    public static UserTokenDTO getGuestUser2(){
        return new UserTokenDTO(3L, "guest2@email.com", "admin", Set.of("USER"));
    }

}
