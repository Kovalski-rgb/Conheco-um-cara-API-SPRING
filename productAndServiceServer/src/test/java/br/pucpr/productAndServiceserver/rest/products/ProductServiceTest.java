package br.pucpr.productAndServiceserver.rest.products;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.pucpr.productAndServiceserver.rest.users.UserMock;
import br.pucpr.productAndServiceserver.rest.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductServiceTest {

	private ProductRepository repository;
	private UserRepository userRepository;
	private ProductService service;

	@BeforeEach
	public void setup() {
		repository = mock(ProductRepository.class);
		userRepository = mock(UserRepository.class);
		service = new ProductService(repository, userRepository);
	}

	@Test
	public void registerShouldSaveAProductSuccessfullyWithExistingUser() {
		when(userRepository.existsById(any())).thenReturn(true);
		when(repository.save(any())).thenReturn(DataMocks.getProduct());
		var result = service.register(UserMock.getUserTokenDTO(), RequestMocks.getProductRequest());

		assertNotNull(result.getOwner());
		assertNotNull(result.getCreatedAt());
	}

	@Test
	public void registerShouldSaveAProductSuccessfullyWithNewUser() {
		when(userRepository.existsById(any())).thenReturn(false);
		when(repository.save(any())).thenReturn(DataMocks.getProduct());
		when(userRepository.save(any())).thenReturn(UserMock.getUser());
		var result = service.register(UserMock.getUserTokenDTO(), RequestMocks.getProductRequest());

		assertNotNull(result.getOwner());
		assertNotNull(result.getCreatedAt());
	}

}
