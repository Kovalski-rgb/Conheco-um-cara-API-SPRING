package br.pucpr.communityserver.rest.community;

import br.pucpr.communityserver.rest.communities.Community;
import br.pucpr.communityserver.rest.communities.requests.CommunityRequest;

import java.util.Random;
import java.util.Set;

public class CommunityStub {

	public static CommunityRequest getCommunity1() {
		var request = new CommunityRequest();
		request.setName("community1");
		request.setDescription("test community 1");
		return request;
	}
	
}
