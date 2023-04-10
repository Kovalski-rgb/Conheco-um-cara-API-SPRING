package br.pucpr.communityserver.rest.post;

import br.pucpr.communityserver.rest.community.MockCommunities;
import br.pucpr.communityserver.rest.posts.Post;
import br.pucpr.communityserver.rest.product.MockProducts;
import br.pucpr.communityserver.rest.service.MockServices;
import br.pucpr.communityserver.rest.user.MockUsers;

import java.time.LocalDateTime;

public class MockPosts {

    public static Post getPost(){
        return new Post(
            1L,
            "mockTitle",
            "mockDescription",
            MockUsers.getUser(),
            LocalDateTime.MIN,
            null,
            MockCommunities.getCommunity(),
            MockServices.getService(),
            MockProducts.getProduct()
        );
    }

}
