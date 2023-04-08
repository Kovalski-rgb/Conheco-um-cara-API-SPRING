package br.pucpr.authserver.rest.users;

import br.pucpr.authserver.rest.users.requests.CreateUserRequest;
import br.pucpr.authserver.rest.users.requests.LoginRequest;

public class UserMock {

    public static CreateUserRequest getUserRequest(){
        return new CreateUserRequest(
            "user",
            "Password#123",
            "email@email.com",
            "1234567890"
        );
    }


    public static User getUser(CreateUserRequest request){
        return new User(request);
    }

}
