package br.pucpr.communityserver.rest.community;

import br.pucpr.communityserver.rest.communities.Community;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

public class MockCommunities {

    public static Community getCommunity(){
        return new Community(
                1L,
                "communityName",
                "communityDescription",
                LocalDateTime.now(),
                new HashSet<>(),
                new HashSet<>(),
                new ArrayList<>(),
                "1234abcd"

        );
    }

}
