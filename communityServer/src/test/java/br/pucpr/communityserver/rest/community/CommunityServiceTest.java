package br.pucpr.communityserver.rest.community;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.pucpr.communityserver.lib.exception.ForbiddenException;
import br.pucpr.communityserver.lib.exception.NotFoundException;
import br.pucpr.communityserver.rest.communities.Community;
import br.pucpr.communityserver.rest.communities.CommunityRepository;
import br.pucpr.communityserver.rest.communities.CommunityService;
import br.pucpr.communityserver.rest.user.MockUsers;
import br.pucpr.communityserver.rest.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
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

	// saveCommunity
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

	// listAllCommunities
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

	// listAllCommunitiesFromUser
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

	// editCommunity
	@Test
	public void editCommunityShouldUpdateChosenCommunityWhenUserHasModeratorPermission(){
		var update = MockRequests.getCommunityRequest();
		var updatedCommunity = MockCommunities.getCommunity();
		update.setName("update");
		updatedCommunity.setName("update");

		when(repository.existsById(any())).thenReturn(true);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(MockUsers.getUser());
		when(repository.findById(any())).thenReturn(Optional.of(MockCommunities.getCommunity()));
		when(repository.getCommunitiesByCodeAndName(any(), any())).thenReturn(null);
		when(repository.save(any())).thenReturn(updatedCommunity);

		var normalUser = MockUsers.getUserTokenDTO();
		var result = service.editCommunity(normalUser, 1L, update);

		assertNotNull(result);
		assertEquals(update.getName(), result.getName());
	}

	@Test
	public void editCommunityShouldUpdateChosenCommunityWhenUserHasOnlyAdminPermission(){
		var update = MockRequests.getCommunityRequest();
		var updatedCommunity = MockCommunities.getCommunity();
		update.setName("update");
		updatedCommunity.setName("update");

		when(repository.existsById(any())).thenReturn(true);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(null);
		when(repository.findById(any())).thenReturn(Optional.of(MockCommunities.getCommunity()));
		when(repository.getCommunitiesByCodeAndName(any(), any())).thenReturn(null);
		when(repository.save(any())).thenReturn(updatedCommunity);

		var adminUser = MockUsers.getAdminUserTokenDTO();
		var result = service.editCommunity(adminUser, 1L, update);

		assertNotNull(result);
		assertEquals(update.getName(), result.getName());
	}

	@Test
	public void editCommunityShouldThrowNotFoundExceptionWhenTargetCommunityDoesNotExist(){
		var update = MockRequests.getCommunityRequest();
		var updatedCommunity = MockCommunities.getCommunity();
		update.setName("update");
		updatedCommunity.setName("update");

		when(repository.existsById(any())).thenReturn(false);

		var normalUser = MockUsers.getUserTokenDTO();

		assertThrows(NotFoundException.class, () -> {
			service.editCommunity(normalUser, 1L, update);
		});
	}

	@Test
	public void editCommunityShouldThrowForbiddenExceptionIsNotAdminNorModerator(){
		var update = MockRequests.getCommunityRequest();
		var updatedCommunity = MockCommunities.getCommunity();
		update.setName("update");
		updatedCommunity.setName("update");

		when(repository.existsById(any())).thenReturn(true);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(null);
		when(repository.findById(any())).thenReturn(Optional.of(MockCommunities.getCommunity()));
		when(repository.getCommunitiesByCodeAndName(any(), any())).thenReturn(null);
		when(repository.save(any())).thenReturn(updatedCommunity);

		var normalUser = MockUsers.getUserTokenDTO();

		assertThrows(ForbiddenException.class, () -> {
			service.editCommunity(normalUser, 1L, update);
		});
	}

	@Test
	public void editCommunityShouldThrowForbiddenExceptionWhenAnotherCommunityMatchesCodeNameCombo(){
		var update = MockRequests.getCommunityRequest();
		var updatedCommunity = MockCommunities.getCommunity();
		update.setName("update");
		updatedCommunity.setName("update");

		when(repository.existsById(any())).thenReturn(true);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(MockUsers.getUser());
		when(repository.findById(any())).thenReturn(Optional.of(MockCommunities.getCommunity()));
		when(repository.getCommunitiesByCodeAndName(any(), any())).thenReturn(updatedCommunity);

		var normalUser = MockUsers.getUserTokenDTO();

		assertThrows(ForbiddenException.class, () -> {
			service.editCommunity(normalUser, 1L, update);
		});
	}

	// deleteCommunity
	@Test
	public void deleteCommunityShouldNotThrowErrorsWhenCommunityExistsAndUserHasModeratorPrivileges(){
		when(repository.existsById(any())).thenReturn(true);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(MockUsers.getUser());

		var mockUserDto = MockUsers.getUserTokenDTO();

		assertDoesNotThrow(()->service.deleteCommunity(mockUserDto, 1L));
	}


	@Test
	public void deleteCommunityShouldNotThrowErrorsWhenCommunityExistsAndUserHasOnlyAdminPrivileges(){
		when(repository.existsById(any())).thenReturn(true);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(null);

		var mockUserDto = MockUsers.getAdminUserTokenDTO();

		assertDoesNotThrow(()->service.deleteCommunity(mockUserDto, 1L));
	}

	@Test
	public void deleteCommunityShouldThrowNotFoundExceptionWhenCommunityDoesNotExist(){
		when(repository.existsById(any())).thenReturn(false);

		var mockUserDto = MockUsers.getUserTokenDTO();
		assertThrows(NotFoundException.class, () -> {
			service.deleteCommunity(mockUserDto, 1L);
		});
	}

	@Test
	public void deleteCommunityShouldThrowForbiddenExceptionWhenUserDoesNotHaveAnyPrivilege(){
		when(repository.existsById(any())).thenReturn(true);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(null);

		var mockUserDto = MockUsers.getUserTokenDTO();

		assertThrows(ForbiddenException.class, () -> {
			service.deleteCommunity(mockUserDto, 1L);
		});
	}

	// joinCommunity
	@Test
	public void joinCommunityShouldLetUserJoinCommunityEvenWhenHeIsNotRegisteredLocally(){
		var request = MockRequests.getCommunityJoinRequest();
		var userDto = MockUsers.getUserTokenDTO();
		var userMock = MockUsers.getUser();

		var community = MockCommunities.getCommunity();
		var savedCommunity = MockCommunities.getCommunity();
		var userList = savedCommunity.getUsers();
		userList.add(userMock);
		savedCommunity.setUsers(userList);

		when(repository.getCommunitiesByCodeAndName(any(), any())).thenReturn(community);
		when(userRepository.existsById(any())).thenReturn(false);
		when(userRepository.save(any())).thenReturn(userMock);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(null);
		when(userRepository.findById(any())).thenReturn(Optional.of(userMock));
		when(repository.save(any())).thenReturn(savedCommunity);

		assertDoesNotThrow(()->service.joinCommunity(userDto, request));
	}

	@Test
	public void joinCommunityShouldLetUserJoinCommunityEvenWhenHeIsRegisteredLocally(){
		var request = MockRequests.getCommunityJoinRequest();
		var userDto = MockUsers.getUserTokenDTO();
		var userMock = MockUsers.getUser();

		var community = MockCommunities.getCommunity();
		var savedCommunity = MockCommunities.getCommunity();
		var userList = savedCommunity.getUsers();
		userList.add(userMock);
		savedCommunity.setUsers(userList);

		when(repository.getCommunitiesByCodeAndName(any(), any())).thenReturn(community);
		when(userRepository.existsById(any())).thenReturn(true);
		when(userRepository.save(any())).thenReturn(userMock);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(null);
		when(userRepository.findById(any())).thenReturn(Optional.of(userMock));
		when(repository.save(any())).thenReturn(savedCommunity);

		assertDoesNotThrow(()->service.joinCommunity(userDto, request));
	}

	@Test
	public void joinCommunityShouldThrowNotFoundExceptionWhenThereIsNotACommunityThatMatchesTheCriteria(){
		var request = MockRequests.getCommunityJoinRequest();
		var userDto = MockUsers.getUserTokenDTO();
		var userMock = MockUsers.getUser();

		var savedCommunity = MockCommunities.getCommunity();
		var userList = savedCommunity.getUsers();
		userList.add(userMock);
		savedCommunity.setUsers(userList);

		when(repository.getCommunitiesByCodeAndName(any(), any())).thenReturn(null);
		when(userRepository.existsById(any())).thenReturn(true);
		when(userRepository.save(any())).thenReturn(userMock);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(null);
		when(userRepository.findById(any())).thenReturn(Optional.of(userMock));
		when(repository.save(any())).thenReturn(savedCommunity);

		assertThrows(NotFoundException.class,()->service.joinCommunity(userDto, request));
	}

	@Test
	public void joinCommunityShouldForbiddenExceptionWhenUserIsAlreadyInsideTheCommunity(){
		var request = MockRequests.getCommunityJoinRequest();
		var userDto = MockUsers.getUserTokenDTO();
		var userMock = MockUsers.getUser();

		var community = MockCommunities.getCommunity();
		var savedCommunity = MockCommunities.getCommunity();
		var userList = savedCommunity.getUsers();
		userList.add(userMock);
		savedCommunity.setUsers(userList);

		when(repository.getCommunitiesByCodeAndName(any(), any())).thenReturn(community);
		when(userRepository.existsById(any())).thenReturn(false);
		when(userRepository.save(any())).thenReturn(userMock);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(userMock);
		when(userRepository.findById(any())).thenReturn(Optional.of(userMock));
		when(repository.save(any())).thenReturn(savedCommunity);

		assertThrows(ForbiddenException.class,()->service.joinCommunity(userDto, request));
	}

}
