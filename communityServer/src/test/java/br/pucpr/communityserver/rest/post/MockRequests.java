package br.pucpr.communityserver.rest.post;

import br.pucpr.communityserver.rest.posts.requests.EditPostRequest;
import br.pucpr.communityserver.rest.posts.requests.PostRequest;

public class MockRequests {

    public static EditPostRequest getEditPostRequest(){
        return new EditPostRequest(
                "editedPost",
                "editedDescription",
                2L,
                2L
        );
    }

    public static PostRequest getPostRequest(){
        return new PostRequest(
                "mockPost",
                "mockDescription",
                1L,
                2L,
                2L
        );
    }

}
