package br.pucpr.authserver.rest.artists;

import br.pucpr.authserver.rest.users.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class UserMock {

    List<User> mockUser;

    public UserMock() {
        mockUser = new ArrayList<>();

        mockUser.add(new User(1L, "mockUser1", "mock1@email.com", "#00Mock1", "telephone", new HashSet<String>().add("USER")));
        mockUser.add(new User());
        mockUser.add(new User());
        mockUser.add(new User());
        mockUser.add(new User());
    }
}
