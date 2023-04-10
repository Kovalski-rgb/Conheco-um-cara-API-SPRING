package br.pucpr.communityserver.rest.community;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.pucpr.communityserver.rest.communities.Community;
import br.pucpr.communityserver.rest.communities.CommunityRepository;
import br.pucpr.communityserver.rest.communities.CommunityService;
import br.pucpr.communityserver.rest.user.MockUsers;
import br.pucpr.communityserver.rest.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommunityServiceTest {

	private CommunityRepository repository;
	private CommunityService service;
	private UserRepository userRepository;

	@BeforeEach
	public void setup() {
		repository = mock(CommunityRepository.class);
		userRepository = mock(UserRepository.class);
		service = new CommunityService(repository, userRepository);
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
		var communityResponse = service.saveCommunity(userDTO, request);

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
		var communityResponse = service.saveCommunity(userDTO, request);

		assertNotNull(communityResponse.getId());
		assertNotNull(communityResponse.getCode());
	}

	@Test
	public void saveCommunityShouldThrowIllegalStateExceptionWhenCodeCheckingBreaks() {
		var userDTO = MockUsers.getUserTokenDTO();
		var request = MockRequests.getCommunityRequest();

		when(repository.getCodesFromAllCommunitiesByName(any())).thenReturn(null);

		assertThrows(IllegalStateException.class, () -> {
			service.saveCommunity(userDTO, request);
		});
	}

	@Test
	public void listAllCommunitiesShouldReturnAListOfCommunities(){
		var list = Stream.of(MockCommunities.getCommunity()).collect(Collectors.toList());
		when(repository.findAll()).thenReturn(list);

		var result = service.listAllCommunities();

		assertTrue(result.size() > 0);
	}

	@Test
	public void listAllCommunitiesShouldReturnAnEmptyList(){
		var list = new ArrayList<Community>();
		when(repository.findAll()).thenReturn(list);

		var result = service.listAllCommunities();

		assertEquals(0, result.size());
	}

	@Test
	public void listAllCommunitiesFromUserShouldReturnAListOfCommunities(){
		var list = Stream.of(MockCommunities.getCommunity()).collect(Collectors.toList());
		when(repository.getAllCommunitiesByUserId(any())).thenReturn(list);

		var result = service.listAllCommunitiesFromUser(1L);

		assertNotNull(result);
		assertTrue(result.size() > 0);
	}

	@Test
	public void listAllCommunitiesFromUserShouldReturnAnEmptyList(){
		var list = new ArrayList<Community>();
		when(repository.getAllCommunitiesByUserId(any())).thenReturn(list);

		var result = service.listAllCommunitiesFromUser(1L);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

}
