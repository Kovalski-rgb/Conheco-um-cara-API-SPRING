package br.pucpr.productAndServiceserver.rest.products;

import br.pucpr.productAndServiceserver.rest.users.UserMock;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataMocks {

    public static Product getProduct(){
        return new Product(
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
