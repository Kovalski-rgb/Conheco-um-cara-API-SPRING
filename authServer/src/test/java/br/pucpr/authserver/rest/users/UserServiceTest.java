package br.pucpr.authserver.rest.users;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.pucpr.authserver.lib.exception.ForbiddenException;
import br.pucpr.authserver.lib.exception.NotFoundException;
import br.pucpr.authserver.lib.security.JWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class UserServiceTest {

	private JWT jwt;
	private UserRepository repository;
	private UserService service;

	@BeforeEach
	public void setup() {
		jwt = mock(JWT.class);
		repository = mock(UserRepository.class);
		service = new UserService(jwt, repository);
	}

	@Test
	public void createUserShouldRegisterNewEmail() {
		var request = UserMock.getUserRequest();
		var user = UserMock.getUser(request);
		when(repository.getUserByEmail(any())).thenReturn(null);
		when(repository.save(any())).thenReturn(user);
		var result = service.createUser(request);

		assertEquals(result.getName(), request.getName());
		assertEquals(result.getEmail(), request.getEmail());
	}
	@Test
	public void createUserShouldThrowForbiddenWhenEmailIsRegistered() {
		var request = UserMock.getUserRequest();
		var user = UserMock.getUser(request);
		when(repository.getUserByEmail(any())).thenReturn(user);
		when(repository.save(any())).thenReturn(user);

		assertThrows(ForbiddenException.class, () -> {
			service.createUser(request);
		});
	}

	@Test
	public void loginShouldLogUserIn(){
		var request = UserMock.getLoginRequest();
		when(repository.getUserByEmail(any())).thenReturn(UserMock.getUser());
		when(jwt.createToken(any())).thenReturn("token");

		var result = service.login(request);
		assertFalse(result.getToken().isEmpty());
		assertNotNull(result.getId());
		assertEquals(request.getEmail(), result.getEmail());
	}


	@Test
	public void loginShouldThrowNotFoundExceptionWhenEmailIsNotRegistered(){
		var request = UserMock.getLoginRequest();
		when(repository.getUserByEmail(any())).thenReturn(null);
		when(jwt.createToken(any())).thenReturn("token");

		assertThrows(NotFoundException.class, () -> {
			service.login(request);
		});
	}

	@Test
	public void loginShouldThrowNotFoundExceptionWhenPasswordDontMatch(){
		var request = UserMock.getLoginRequest();
		request.setPassword("wrongPassword#234");
		when(repository.getUserByEmail(any())).thenReturn(UserMock.getUser());
		when(jwt.createToken(any())).thenReturn("token");

		assertThrows(NotFoundException.class, () -> {
			service.login(request);
		});
	}

	@Test
	public void getUserShouldReturnReturnValidUserById(){
		var request = UserMock.getUser();
		when(repository.existsById(any())).thenReturn(true);
		when(repository.findById(any())).thenReturn(Optional.of(UserMock.getUser()));

		var result = service.getUser(request.getId());

		assertEquals(request.getId(), result.getId());
		assertEquals(request.getEmail(), result.getEmail());
		assertEquals(request.getName(), result.getName());
	}

	@Test
	public void getUserShouldThrowNotFoundExceptionWhenIdIsNotFound(){
		var request = UserMock.getUser();
		request.setPassword("wrongPassword#234");
		when(repository.existsById(any())).thenReturn(false);
		when(repository.findById(any())).thenReturn(Optional.of(UserMock.getUser()));

		assertThrows(NotFoundException.class, ()-> {
			service.getUser(request.getId());
		});

	}

}
