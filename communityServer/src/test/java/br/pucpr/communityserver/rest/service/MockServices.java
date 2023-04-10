package br.pucpr.communityserver.rest.service;

import br.pucpr.communityserver.rest.services.Service;
import br.pucpr.communityserver.rest.user.MockUsers;

public class MockServices {

    public static Service getService() {
        return new Service(
                1L,
                MockUsers.getUser()
        );
    }

}
