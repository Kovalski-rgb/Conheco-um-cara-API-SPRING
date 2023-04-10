package br.pucpr.productAndServiceserver.rest.services;

import br.pucpr.productAndServiceserver.rest.users.UserMock;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServiceMocks {

    public static Service getService(){
        return new Service(
                1L,
                "mockName",
                "mockDescription",
                UserMock.getUser(),
                12.50,
                Stream.of("Mock").collect(Collectors.toSet()),
                LocalDateTime.MIN
        );
    }

}
