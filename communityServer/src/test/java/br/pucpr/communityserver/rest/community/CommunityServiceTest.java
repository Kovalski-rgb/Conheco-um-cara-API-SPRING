package br.pucpr.communityserver.rest.community;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.pucpr.communityserver.rest.communities.CommunityRepository;
import br.pucpr.communityserver.rest.communities.CommunityService;
import br.pucpr.communityserver.rest.user.MockUsers;
import br.pucpr.communityserver.rest.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

public class CommunityServiceTest {

	private CommunityRepository repository;
	private CommunityService communityService;
	private UserRepository userRepository;

	@BeforeEach
	public void setup() {
		repository = mock(CommunityRepository.class);
		userRepository = mock(UserRepository.class);
		communityService = new CommunityService(repository, userRepository);
	}

	@Test
	public void saveCommunityShouldCreateCommunitySuccessfullyWhenUserExists() {
		var userDTO = MockUsers.getUserTokenDTO();
		var request = MockRequests.getCommunityRequest();
		var communityMock = MockCommunities.getCommunity();

		when(repository.getCodesFromAllCommunitiesByName(any())).thenReturn(new HashSet<>());
		when(repository.save(any())).thenReturn(communityMock);
		when(userRepository.existsById(any())).thenReturn(true);
		when(userRepository.save(any())).thenReturn(MockUsers.getUser());
		var communityResponse = communityService.saveCommunity(userDTO, request);

		assertNotNull(communityResponse.getId());
		assertNotNull(communityResponse.getCode());
	}

	@Test
	public void saveCommunityShouldCreateCommunitySuccessfullyWhenUserDoNotExistYet() {
		var userDTO = MockUsers.getUserTokenDTO();
		var request = MockRequests.getCommunityRequest();
		var communityMock = MockCommunities.getCommunity();

		when(repository.getCodesFromAllCommunitiesByName(any())).thenReturn(new HashSet<>());
		when(repository.save(any())).thenReturn(communityMock);
		when(userRepository.existsById(any())).thenReturn(false);
		when(userRepository.save(any())).thenReturn(MockUsers.getUser());
		var communityResponse = communityService.saveCommunity(userDTO, request);

		assertNotNull(communityResponse.getId());
		assertNotNull(communityResponse.getCode());
	}

	@Test
	public void saveCommunityShouldThrowIllegalStateExceptionWhenCodeCheckingBreaks() {
		var userDTO = MockUsers.getUserTokenDTO();
		var request = MockRequests.getCommunityRequest();

		when(repository.getCodesFromAllCommunitiesByName(any())).thenReturn(null);

		assertThrows(IllegalStateException.class, () -> {
			communityService.saveCommunity(userDTO, request);
		});
	}

}
