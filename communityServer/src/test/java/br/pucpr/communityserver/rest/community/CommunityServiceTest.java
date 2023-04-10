package br.pucpr.communityserver.rest.community;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

		assertThrows(IllegalStateException.class, () -> service.saveCommunity(userDTO, request));
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

		assertThrows(NotFoundException.class, () -> service.editCommunity(normalUser, 1L, update));
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

		assertThrows(ForbiddenException.class, () -> service.editCommunity(normalUser, 1L, update));
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

		assertThrows(ForbiddenException.class, () -> service.editCommunity(normalUser, 1L, update));
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
		assertThrows(NotFoundException.class, () -> service.deleteCommunity(mockUserDto, 1L));
	}

	@Test
	public void deleteCommunityShouldThrowForbiddenExceptionWhenUserDoesNotHaveAnyPrivilege(){
		when(repository.existsById(any())).thenReturn(true);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(null);

		var mockUserDto = MockUsers.getUserTokenDTO();

		assertThrows(ForbiddenException.class, () -> service.deleteCommunity(mockUserDto, 1L));
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

	// leaveCommunity
	@Test
	public void leaveCommunityShouldLetUserLeaveCommunityAndNotDeleteItWhenItHasMoreThanOneUser(){
		var userDto = MockUsers.getUserTokenDTO();
		var userMock = MockUsers.getUser();
		var otherUserMock = MockUsers.getUser();
		otherUserMock.setId(2L);
		var communityMock = MockCommunities.getCommunity();
		var userList = Stream.of(userMock, otherUserMock).toList();


		when(repository.existsById(any())).thenReturn(true);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(userMock);
		when(repository.getCommunityById(any())).thenReturn(communityMock);
		when(userRepository.findById(any())).thenReturn(Optional.of(userMock));
		when(repository.getModeratorListFromCommunityById(any())).thenReturn(userList);
		when(repository.getUserListFromCommunityById(any())).thenReturn(userList);

		assertDoesNotThrow(()->service.leaveCommunity(userDto, 1L));
		verify(repository, times(0)).deleteCommunityById(any());
	}

	@Test
	public void leaveCommunityShouldLetUserLeaveCommunityAndDeleteItWhenUserWasTheLastUserInsideCommunity(){
		var userDto = MockUsers.getUserTokenDTO();
		var userMock = MockUsers.getUser();
		var communityMock = MockCommunities.getCommunity();

		when(repository.existsById(any())).thenReturn(true);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(userMock);
		when(repository.getCommunityById(any())).thenReturn(communityMock);
		when(userRepository.findById(any())).thenReturn(Optional.of(userMock));
		when(repository.getModeratorListFromCommunityById(any())).thenReturn(new ArrayList<>());
		when(repository.getUserListFromCommunityById(any())).thenReturn(new ArrayList<>());

		assertDoesNotThrow(()->service.leaveCommunity(userDto, 1L));
		verify(repository, times(1)).deleteCommunityById(any());
	}

	@Test
	public void leaveCommunityShouldThrowNotFoundExceptionWhenCommunityDoesNotExist(){
		var userDto = MockUsers.getUserTokenDTO();
		var userMock = MockUsers.getUser();
		var otherUserMock = MockUsers.getUser();
		otherUserMock.setId(2L);
		var communityMock = MockCommunities.getCommunity();
		var userList = Stream.of(userMock, otherUserMock).toList();


		when(repository.existsById(any())).thenReturn(false);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(userMock);
		when(repository.getCommunityById(any())).thenReturn(communityMock);
		when(userRepository.findById(any())).thenReturn(Optional.of(userMock));
		when(repository.getModeratorListFromCommunityById(any())).thenReturn(userList);
		when(repository.getUserListFromCommunityById(any())).thenReturn(userList);

		assertThrows(NotFoundException.class, ()->service.leaveCommunity(userDto, 1L));
	}

	@Test
	public void leaveCommunityShouldThrowNotFoundExceptionWhenUserIsNotInsideCommunity(){
		var userDto = MockUsers.getUserTokenDTO();
		var userMock = MockUsers.getUser();
		var otherUserMock = MockUsers.getUser();
		otherUserMock.setId(2L);
		var communityMock = MockCommunities.getCommunity();
		var userList = Stream.of(userMock, otherUserMock).toList();


		when(repository.existsById(any())).thenReturn(true);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(null);
		when(repository.getCommunityById(any())).thenReturn(communityMock);
		when(userRepository.findById(any())).thenReturn(Optional.of(userMock));
		when(repository.getModeratorListFromCommunityById(any())).thenReturn(userList);
		when(repository.getUserListFromCommunityById(any())).thenReturn(userList);

		assertThrows(NotFoundException.class, ()->service.leaveCommunity(userDto, 1L));
	}

	// kickFromCommunity
	@Test
	public void kickFromCommunityShouldKickAnotherUserFromCommunityWhenUserIsModeratorAndNotDeleteCommunityWhenSizeIsBiggerThanOne(){
		var userDto = MockUsers.getUserTokenDTO();
		var userMock = MockUsers.getUser();
		var communityMock = MockCommunities.getCommunity();
		var userList = Stream.of(userMock).toList();

		when(repository.existsById(any())).thenReturn(true);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(userMock);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(userMock);
		when(repository.getCommunityById(any())).thenReturn(communityMock);
		when(userRepository.findById(any())).thenReturn(Optional.of(userMock));
		when(repository.getModeratorListFromCommunityById(any())).thenReturn(userList);
		when(repository.save(any())).thenReturn(communityMock);
		when(repository.getUserListFromCommunityById(any())).thenReturn(userList);

		assertDoesNotThrow(()->service.kickFromCommunity(userDto, 1L, 1L));
		verify(repository, times(0)).deleteCommunityById(any());
	}

	@Test
	public void kickFromCommunityShouldKickAnotherUserFromCommunityWhenUserIsAdminAndNotDeleteCommunityWhenSizeIsBiggerThanOne(){
		var userDto = MockUsers.getAdminUserTokenDTO();
		var userMock = MockUsers.getUser();
		var communityMock = MockCommunities.getCommunity();
		var userList = Stream.of(userMock).toList();

		when(repository.existsById(any())).thenReturn(true);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(userMock);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(null);
		when(repository.getCommunityById(any())).thenReturn(communityMock);
		when(userRepository.findById(any())).thenReturn(Optional.of(userMock));
		when(repository.getModeratorListFromCommunityById(any())).thenReturn(userList);
		when(repository.save(any())).thenReturn(communityMock);
		when(repository.getUserListFromCommunityById(any())).thenReturn(userList);

		assertDoesNotThrow(()->service.kickFromCommunity(userDto, 1L, 1L));
		verify(repository, times(0)).deleteCommunityById(any());
	}

	@Test
	public void kickFromCommunityShouldThrowNotFoundExceptionWhenCommunityIsNotFound(){
		var userDto = MockUsers.getUserTokenDTO();
		var userMock = MockUsers.getUser();
		var communityMock = MockCommunities.getCommunity();
		var userList = Stream.of(userMock).toList();

		when(repository.existsById(any())).thenReturn(false);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(userMock);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(userMock);
		when(repository.getCommunityById(any())).thenReturn(communityMock);
		when(userRepository.findById(any())).thenReturn(Optional.of(userMock));
		when(repository.getModeratorListFromCommunityById(any())).thenReturn(userList);
		when(repository.save(any())).thenReturn(communityMock);
		when(repository.getUserListFromCommunityById(any())).thenReturn(userList);

		assertThrows(NotFoundException.class,()->service.kickFromCommunity(userDto, 1L, 1L));
	}

	@Test
	public void kickFromCommunityShouldThrowForbiddenExceptionWhenTargetDoesNotBelongToCommunity(){
		var userDto = MockUsers.getUserTokenDTO();
		var userMock = MockUsers.getUser();
		var communityMock = MockCommunities.getCommunity();
		var userList = Stream.of(userMock).toList();

		when(repository.existsById(any())).thenReturn(true);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(null);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(userMock);
		when(repository.getCommunityById(any())).thenReturn(communityMock);
		when(userRepository.findById(any())).thenReturn(Optional.of(userMock));
		when(repository.getModeratorListFromCommunityById(any())).thenReturn(userList);
		when(repository.save(any())).thenReturn(communityMock);
		when(repository.getUserListFromCommunityById(any())).thenReturn(userList);

		assertThrows(ForbiddenException.class,()->service.kickFromCommunity(userDto, 1L, 1L));
	}

	@Test
	public void kickFromCommunityShouldThrowForbiddenExceptionWhenUserDoesNotHaveAnyPrivileges(){
		var userDto = MockUsers.getUserTokenDTO();
		var userMock = MockUsers.getUser();
		var communityMock = MockCommunities.getCommunity();
		var userList = Stream.of(userMock).toList();

		when(repository.existsById(any())).thenReturn(true);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(userMock);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(null);
		when(repository.getCommunityById(any())).thenReturn(communityMock);
		when(userRepository.findById(any())).thenReturn(Optional.of(userMock));
		when(repository.getModeratorListFromCommunityById(any())).thenReturn(userList);
		when(repository.save(any())).thenReturn(communityMock);
		when(repository.getUserListFromCommunityById(any())).thenReturn(userList);

		assertThrows(ForbiddenException.class,()->service.kickFromCommunity(userDto, 1L, 1L));
	}

	// getCommunityById
	@Test
	public void getCommunityByIdShouldReturnACommunity(){
		var community = MockCommunities.getCommunity();

		when(repository.existsById(any())).thenReturn(true);
		when(repository.findById(any())).thenReturn(Optional.of(community));

		var result = service.getCommunityById(1L);

		assertNotNull(result);
	}

	@Test
	public void getCommunityByIdShouldReturnNullWhenCommunityDoesNotExist(){
		var community = MockCommunities.getCommunity();

		when(repository.existsById(any())).thenReturn(false);
		when(repository.findById(any())).thenReturn(Optional.of(community));

		var result = service.getCommunityById(1L);

		assertNull(result);
	}

	// listAllModeratorsFromCommunity
	@Test
	public void listAllModeratorsFromCommunityShouldListModeratorsWhenUserIsInsideCommunity(){
		var userMock = MockUsers.getUser();
		var userList = Stream.of(userMock).toList();

		when(repository.existsById(any())).thenReturn(true);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(userMock);
		when(repository.getModeratorListFromCommunityById(any())).thenReturn(userList);

		assertDoesNotThrow(() -> {
			var result = service.listAllModeratorsFromCommunity(1L, 1L);
			assertNotNull(result);
			assertTrue(result.size()>0);
		});
	}

	@Test
	public void listAllModeratorsFromCommunityShouldThrowNotFoundExceptionWhenCommunityDoesNotExist(){
		var userMock = MockUsers.getUser();
		var userList = Stream.of(userMock).toList();

		when(repository.existsById(any())).thenReturn(false);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(userMock);
		when(repository.getModeratorListFromCommunityById(any())).thenReturn(userList);

		assertThrows(NotFoundException.class, ()->service.listAllModeratorsFromCommunity(1L, 1L));
	}

	@Test
	public void listAllModeratorsFromCommunityShouldThrowForbiddenExceptionWhenUserDoesNotBelongToTheCommunity(){
		var userMock = MockUsers.getUser();
		var userList = Stream.of(userMock).toList();

		when(repository.existsById(any())).thenReturn(true);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(null);
		when(repository.getModeratorListFromCommunityById(any())).thenReturn(userList);

		assertThrows(ForbiddenException.class, ()->service.listAllModeratorsFromCommunity(1L, 1L));
	}

	// listMembersFromCommunity
	@Test
	public void listMembersFromCommunityShouldListMembersFromCommunityWhenUserBelongsToThatCommunity(){
		var userMock = MockUsers.getUser();
		var userList = Stream.of(userMock).toList();

		when(repository.existsById(any())).thenReturn(true);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(userMock);
		when(repository.getUserListFromCommunityById(any())).thenReturn(userList);

		assertDoesNotThrow(() -> {
			var result = service.listMembersFromCommunity(1L, 1L);
			assertNotNull(result);
			assertTrue(result.size()>0);
		});
	}

	@Test
	public void listMembersFromCommunityShouldThrowNotFoundExceptionWhenCommunityDoesNotExist(){
		var userMock = MockUsers.getUser();
		var userList = Stream.of(userMock).toList();

		when(repository.existsById(any())).thenReturn(false);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(userMock);
		when(repository.getUserListFromCommunityById(any())).thenReturn(userList);

		assertThrows(NotFoundException.class, ()->service.listAllModeratorsFromCommunity(1L, 1L));
	}

	@Test
	public void listMembersFromCommunityShouldThrowForbiddenExceptionWhenUserDoesNotBelongToTheCommunity(){
		var userMock = MockUsers.getUser();
		var userList = Stream.of(userMock).toList();

		when(repository.existsById(any())).thenReturn(true);
		when(repository.getUserInCommunityById(any(), any())).thenReturn(null);
		when(repository.getUserListFromCommunityById(any())).thenReturn(userList);

		assertThrows(ForbiddenException.class, ()->service.listAllModeratorsFromCommunity(1L, 1L));
	}

	// toggleModerator
	@Test
	public void toggleModeratorShouldGrantModeratorPermissionsForTargetUser(){
		var communityMock = MockCommunities.getCommunity();
		var communityMockResponse = MockCommunities.getCommunity();
		var modUserDTO = MockUsers.getUserTokenDTO();
		var modUser = MockUsers.getUser();
		var targetUser = MockUsers.getUser();
		var request = MockRequests.getRequestToggleModerator();

		modUserDTO.setId(modUser.getId());
		targetUser.setId(123L);
		request.setUserId(targetUser.getId());

		communityMock.setModerators(Stream.of(modUser).collect(Collectors.toSet()));
		communityMock.setUsers(Stream.of(modUser, targetUser).collect(Collectors.toSet()));

		communityMockResponse.setModerators(Stream.of(modUser, targetUser).collect(Collectors.toSet()));
		communityMockResponse.setUsers(Stream.of(modUser, targetUser).collect(Collectors.toSet()));


		when(repository.existsById(any())).thenReturn(true);
		when(repository.getUserInCommunityById(any(), eq(modUser.getId()))).thenReturn(modUser);
		when(repository.getUserInCommunityById(any(), eq(targetUser.getId()))).thenReturn(targetUser);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(modUser);
		when(repository.getCommunityById(any())).thenReturn(communityMock);
		when(userRepository.findById(any())).thenReturn(Optional.of(targetUser));
		when(repository.save(any())).thenReturn(communityMockResponse);

		assertDoesNotThrow(() -> service.toggleModerator(modUserDTO, request));
		verify(userRepository, times(1)).findById(any());
		verify(repository, times(1)).save(any());
	}

	@Test
	public void toggleModeratorShouldStripModeratorPermissionsForTargetUser(){
		var communityMock = MockCommunities.getCommunity();
		var communityMockResponse = MockCommunities.getCommunity();
		var modUserDTO = MockUsers.getUserTokenDTO();
		var modUser = MockUsers.getUser();
		var targetUser = MockUsers.getUser();
		var request = MockRequests.getRequestToggleModerator();

		modUserDTO.setId(modUser.getId());
		targetUser.setId(123L);
		request.setUserId(targetUser.getId());

		communityMock.setModerators(Stream.of(modUser, targetUser).collect(Collectors.toSet()));
		communityMock.setUsers(Stream.of(modUser, targetUser).collect(Collectors.toSet()));

		communityMockResponse.setModerators(Stream.of(modUser, targetUser).collect(Collectors.toSet()));
		communityMockResponse.setUsers(Stream.of(modUser, targetUser).collect(Collectors.toSet()));


		when(repository.existsById(any())).thenReturn(true);
		when(repository.getUserInCommunityById(any(), eq(modUser.getId()))).thenReturn(modUser);
		when(repository.getUserInCommunityById(any(), eq(targetUser.getId()))).thenReturn(targetUser);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(modUser);
		when(repository.getCommunityById(any())).thenReturn(communityMock);
		when(userRepository.findById(any())).thenReturn(Optional.of(targetUser));
		when(repository.save(any())).thenReturn(communityMockResponse);

		assertDoesNotThrow(() -> service.toggleModerator(modUserDTO, request));
		verify(userRepository, times(0)).findById(any());
		verify(repository, times(1)).save(any());
	}

	@Test
	public void toggleModeratorShouldThrowNotFoundExceptionWhenCommunityDoesNotExist(){
		var communityMock = MockCommunities.getCommunity();
		var communityMockResponse = MockCommunities.getCommunity();
		var modUserDTO = MockUsers.getUserTokenDTO();
		var modUser = MockUsers.getUser();
		var targetUser = MockUsers.getUser();
		var request = MockRequests.getRequestToggleModerator();

		modUserDTO.setId(modUser.getId());
		targetUser.setId(123L);
		request.setUserId(targetUser.getId());

		communityMock.setModerators(Stream.of(modUser).collect(Collectors.toSet()));
		communityMock.setUsers(Stream.of(modUser, targetUser).collect(Collectors.toSet()));

		communityMockResponse.setModerators(Stream.of(modUser, targetUser).collect(Collectors.toSet()));
		communityMockResponse.setUsers(Stream.of(modUser, targetUser).collect(Collectors.toSet()));


		when(repository.existsById(any())).thenReturn(false);
		when(repository.getUserInCommunityById(any(), eq(modUser.getId()))).thenReturn(modUser);
		when(repository.getUserInCommunityById(any(), eq(targetUser.getId()))).thenReturn(targetUser);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(modUser);
		when(repository.getCommunityById(any())).thenReturn(communityMock);
		when(userRepository.findById(any())).thenReturn(Optional.of(targetUser));
		when(repository.save(any())).thenReturn(communityMockResponse);

		assertThrows(NotFoundException.class, () -> service.toggleModerator(modUserDTO, request));
		verify(userRepository, times(0)).findById(any());
		verify(repository, times(0)).save(any());
	}

	@Test
	public void toggleModeratorShouldThrowForbiddenExceptionWhenUserDoesNotBelongToThatCommunity(){
		var communityMock = MockCommunities.getCommunity();
		var communityMockResponse = MockCommunities.getCommunity();
		var modUserDTO = MockUsers.getUserTokenDTO();
		var modUser = MockUsers.getUser();
		var targetUser = MockUsers.getUser();
		var request = MockRequests.getRequestToggleModerator();

		modUserDTO.setId(modUser.getId());
		targetUser.setId(123L);
		request.setUserId(targetUser.getId());

		communityMock.setModerators(Stream.of(modUser).collect(Collectors.toSet()));
		communityMock.setUsers(Stream.of(modUser, targetUser).collect(Collectors.toSet()));

		communityMockResponse.setModerators(Stream.of(modUser, targetUser).collect(Collectors.toSet()));
		communityMockResponse.setUsers(Stream.of(modUser, targetUser).collect(Collectors.toSet()));


		when(repository.existsById(any())).thenReturn(true);
		when(repository.getUserInCommunityById(any(), eq(modUser.getId()))).thenReturn(null);
		when(repository.getUserInCommunityById(any(), eq(targetUser.getId()))).thenReturn(targetUser);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(modUser);
		when(repository.getCommunityById(any())).thenReturn(communityMock);
		when(userRepository.findById(any())).thenReturn(Optional.of(targetUser));
		when(repository.save(any())).thenReturn(communityMockResponse);

		assertThrows(ForbiddenException.class, () -> service.toggleModerator(modUserDTO, request));
		verify(userRepository, times(0)).findById(any());
		verify(repository, times(0)).save(any());
	}

	@Test
	public void toggleModeratorShouldThrowForbiddenExceptionWhenTargetUserDoesNotBelongToThatCommunity(){
		var communityMock = MockCommunities.getCommunity();
		var communityMockResponse = MockCommunities.getCommunity();
		var modUserDTO = MockUsers.getUserTokenDTO();
		var modUser = MockUsers.getUser();
		var targetUser = MockUsers.getUser();
		var request = MockRequests.getRequestToggleModerator();

		modUserDTO.setId(modUser.getId());
		targetUser.setId(123L);
		request.setUserId(targetUser.getId());

		communityMock.setModerators(Stream.of(modUser).collect(Collectors.toSet()));
		communityMock.setUsers(Stream.of(modUser, targetUser).collect(Collectors.toSet()));

		communityMockResponse.setModerators(Stream.of(modUser, targetUser).collect(Collectors.toSet()));
		communityMockResponse.setUsers(Stream.of(modUser, targetUser).collect(Collectors.toSet()));


		when(repository.existsById(any())).thenReturn(true);
		when(repository.getUserInCommunityById(any(), eq(modUser.getId()))).thenReturn(modUser);
		when(repository.getUserInCommunityById(any(), eq(targetUser.getId()))).thenReturn(null);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(modUser);
		when(repository.getCommunityById(any())).thenReturn(communityMock);
		when(userRepository.findById(any())).thenReturn(Optional.of(targetUser));
		when(repository.save(any())).thenReturn(communityMockResponse);

		assertThrows(ForbiddenException.class, () -> service.toggleModerator(modUserDTO, request));
		verify(userRepository, times(0)).findById(any());
		verify(repository, times(0)).save(any());
	}

	@Test
	public void toggleModeratorShouldThrowForbiddenExceptionWhenUserDoesNotHaveModeratorPrivilegesToToggleOtherModerator(){
		var communityMock = MockCommunities.getCommunity();
		var communityMockResponse = MockCommunities.getCommunity();
		var modUserDTO = MockUsers.getUserTokenDTO();
		var modUser = MockUsers.getUser();
		var targetUser = MockUsers.getUser();
		var request = MockRequests.getRequestToggleModerator();

		modUserDTO.setId(modUser.getId());
		targetUser.setId(123L);
		request.setUserId(targetUser.getId());

		communityMock.setModerators(Stream.of(targetUser).collect(Collectors.toSet()));
		communityMock.setUsers(Stream.of(modUser, targetUser).collect(Collectors.toSet()));

		communityMockResponse.setModerators(Stream.of(targetUser).collect(Collectors.toSet()));
		communityMockResponse.setUsers(Stream.of(modUser, targetUser).collect(Collectors.toSet()));


		when(repository.existsById(any())).thenReturn(true);
		when(repository.getUserInCommunityById(any(), eq(modUser.getId()))).thenReturn(modUser);
		when(repository.getUserInCommunityById(any(), eq(targetUser.getId()))).thenReturn(null);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(modUser);
		when(repository.getCommunityById(any())).thenReturn(communityMock);
		when(userRepository.findById(any())).thenReturn(Optional.of(targetUser));
		when(repository.save(any())).thenReturn(communityMockResponse);

		assertThrows(ForbiddenException.class, () -> service.toggleModerator(modUserDTO, request));
		verify(userRepository, times(0)).findById(any());
		verify(repository, times(0)).save(any());
	}

	@Test
	public void toggleModeratorShouldThrowForbiddenExceptionWhenUserIsTheLastModeratorInsideCommunity(){
		var communityMock = MockCommunities.getCommunity();
		var communityMockResponse = MockCommunities.getCommunity();
		var modUserDTO = MockUsers.getUserTokenDTO();
		var modUser = MockUsers.getUser();
		var targetUser = MockUsers.getUser();
		var request = MockRequests.getRequestToggleModerator();

		modUserDTO.setId(modUser.getId());
		targetUser.setId(modUser.getId());
		request.setUserId(targetUser.getId());

		communityMock.setModerators(Stream.of(modUser).collect(Collectors.toSet()));
		communityMock.setUsers(Stream.of(modUser).collect(Collectors.toSet()));

		when(repository.existsById(any())).thenReturn(true);
		when(repository.getUserInCommunityById(any(), eq(modUser.getId()))).thenReturn(modUser);
		when(repository.getUserInCommunityById(any(), eq(targetUser.getId()))).thenReturn(null);
		when(repository.getModeratorByCommunityAndUser(any(), any())).thenReturn(modUser);
		when(repository.getCommunityById(any())).thenReturn(communityMock);
		when(userRepository.findById(any())).thenReturn(Optional.of(targetUser));
		when(repository.save(any())).thenReturn(communityMockResponse);

		assertThrows(ForbiddenException.class, () -> {
			service.toggleModerator(modUserDTO, request);
		});
		verify(userRepository, times(0)).findById(any());
		verify(repository, times(0)).save(any());
	}

}

