package br.pucpr.authserver.rest.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.pucpr.authserver.lib.exception.ForbiddenException;
import br.pucpr.authserver.lib.security.JWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTest {

	private JWT jwt;
	private UserRepository repository;
	private UserService service;

	@BeforeEach
	public void setup() {
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
	
}
