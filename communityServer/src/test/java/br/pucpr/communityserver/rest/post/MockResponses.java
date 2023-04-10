package br.pucpr.communityserver.rest.post;

import br.pucpr.communityserver.rest.posts.response.PostResponse;

import java.time.LocalDateTime;

public class MockResponses {

    public static PostResponse getPostResponse(){
        return new PostResponse(
                1L,
                "mockTitle",
                "mockDescription",
                1L,
                LocalDateTime.MIN,
                1L,
                1L,
                1L
        );
    }

}
