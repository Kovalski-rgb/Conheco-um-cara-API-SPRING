package br.pucpr.communityserver.rest.community;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.pucpr.communityserver.rest.communities.Community;
import br.pucpr.communityserver.rest.communities.CommunityRepository;
import br.pucpr.communityserver.rest.communities.CommunityService;
import br.pucpr.communityserver.rest.communities.requests.CommunityJoinRequest;
import br.pucpr.communityserver.rest.communities.responses.CommunityResponse;
import br.pucpr.communityserver.rest.users.User;
import br.pucpr.communityserver.rest.users.UserRepository;
import br.pucpr.communityserver.rest.users.requests.UserTokenDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.pucpr.communityserver.lib.exception.BadRequestException;

public class CommunityServiceTest {

	private CommunityRepository communityRepository;
	private CommunityService communityService;

	@BeforeEach
	public void setup() {
		communityRepository = mock(CommunityRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		communityService = new CommunityService(communityRepository, userRepository);
	}

	@Test
	public void should_create_community_successfully() {
		var userDTO = UserDtoStub.getAdminUser();
		var request = CommunityStub.getCommunityRequest1();
		var community = new Community(request);
		community.setId(1L);
		community.setCreatedAt(LocalDateTime.now());
		community.setCode(communityService.generateCode(8));
		community.setModerators(new HashSet<User>());
		community.setUsers(new HashSet<User>());

		when(communityRepository.save(any())).thenReturn(community);
		var communityResponse = communityService.saveCommunity(userDTO, request);

		assertNotNull(communityResponse);
		assertNotNull(communityResponse.getCode());
	}

	@Test
	public void should_let_user_join_community() {
		var userDTO = UserDtoStub.getAdminUser();
	}

}
