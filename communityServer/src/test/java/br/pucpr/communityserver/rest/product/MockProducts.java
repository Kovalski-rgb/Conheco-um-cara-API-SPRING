package br.pucpr.communityserver.rest.product;

import br.pucpr.communityserver.rest.products.Product;
import br.pucpr.communityserver.rest.user.MockUsers;

public class MockProducts {

    public static Product getProduct() {
        return new Product(
                1L,
                MockUsers.getUser()
        );
    }

}
