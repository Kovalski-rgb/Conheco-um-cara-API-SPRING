package br.pucpr.communityserver.rest.community;

import br.pucpr.communityserver.rest.communities.responses.CommunityResponse;
import br.pucpr.communityserver.rest.communities.responses.GetModeratorResponse;
import br.pucpr.communityserver.rest.communities.responses.MultipleCommunitiesResponse;

import java.time.LocalDateTime;

public class MockResponses {

    public static CommunityResponse getCommunityResponse(){
        return new CommunityResponse(
            1L,
            "communityName",
            "communityDescription",
            "1234abcd",
            LocalDateTime.now()
        );
    }

    public static GetModeratorResponse getGetModeratorResponse(){
        return new GetModeratorResponse(1L);
    }

    public static MultipleCommunitiesResponse getMultipleCommunitiesResponse(){
        return new MultipleCommunitiesResponse(
            1L,
            "communityName",
            "communityDescription",
            LocalDateTime.now()

        );
    }

}
