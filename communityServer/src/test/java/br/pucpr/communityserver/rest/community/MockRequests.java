package br.pucpr.communityserver.rest.community;

import br.pucpr.communityserver.rest.communities.requests.CommunityJoinRequest;
import br.pucpr.communityserver.rest.communities.requests.CommunityRequest;
import br.pucpr.communityserver.rest.communities.requests.RequestToggleModerator;

public class MockRequests {

    public static CommunityJoinRequest getCommunityJoinRequest(){
        return new CommunityJoinRequest(
                "communityName",
                "1234abcd"
        );
    }

    public static CommunityRequest getCommunityRequest(){
        return new CommunityRequest(
                "communityName",
                "communityDescription"
        );
    }

    public static RequestToggleModerator getRequestToggleModerator(){
        return new RequestToggleModerator(
                1L,
                1L
        );
    }

}
