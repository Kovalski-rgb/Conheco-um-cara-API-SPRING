package br.pucpr.productAndServiceserver.rest.products;

import br.pucpr.productAndServiceserver.rest.users.UserMock;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductMocks {

    public static Product getProduct(){
        return new Product(
            1L,
            "mockName",
            "mockDescription",
            UserMock.getUser(),
            12.50,
            LocalDateTime.MIN
        );
    }


}
