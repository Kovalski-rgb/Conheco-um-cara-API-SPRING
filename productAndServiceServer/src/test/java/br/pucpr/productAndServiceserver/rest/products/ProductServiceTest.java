package br.pucpr.productAndServiceserver.rest.products;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.pucpr.productAndServiceserver.lib.exception.ForbiddenException;
import br.pucpr.productAndServiceserver.lib.exception.NotFoundException;
import br.pucpr.productAndServiceserver.rest.users.UserMock;
import br.pucpr.productAndServiceserver.rest.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	@Test
	public void listAllShouldReturnSomeProducts(){
		var list = Stream.of(DataMocks.getProduct(),DataMocks.getProduct(),DataMocks.getProduct()).collect(Collectors.toList());
		when(repository.selectAllProducts(any())).thenReturn(list);
		when(repository.countAllProducts()).thenReturn(list.size());
		var response = service.listAllProducts(0);

		assertNotNull(response.getProducts());
		assertNotNull(response.getLastPage());
		assertNotNull(response.getPageNumber());
	}

	@Test
	public void listAllShouldNotActWeirdWhenThereAreNoProducts(){
		when(repository.selectAllProducts(any())).thenReturn(new ArrayList<>());
		when(repository.countAllProducts()).thenReturn(0);
		var response = service.listAllProducts(0);

		assertNotNull(response.getProducts());
		assertNotNull(response.getLastPage());
		assertNotNull(response.getPageNumber());
	}

	@Test
	public void listFromUserShouldReturnUserProducts(){
		when(userRepository.existsById(any())).thenReturn(true);
		var list = Stream.of(DataMocks.getProduct(),DataMocks.getProduct()).collect(Collectors.toList());
		when(repository.getProductsByOwnerId(any(), any())).thenReturn(list);
		when(repository.countProductsByOwnerId(any())).thenReturn(list.size());

		var response = service.listFromUser(1L, 0);
		assertNotNull(response.getProducts());
		assertNotNull(response.getLastPage());
		assertNotNull(response.getPageNumber());
	}

	@Test
	public void listFromUserShouldThrowNotFoundExceptionWhenUserIsNotInDatabase() {
		when(userRepository.existsById(any())).thenReturn(false);
		assertThrows(NotFoundException.class, () -> {
			service.listFromUser(1L, 0);
		});
	}

	@Test
	public void listFromUserShouldNotActWeirdWhenThereAreNoProducts(){
		when(userRepository.existsById(any())).thenReturn(true);
		when(repository.getProductsByOwnerId(any(), any())).thenReturn(new ArrayList<>());
		when(repository.countProductsByOwnerId(any())).thenReturn(0);

		var response = service.listFromUser(1L, 0);
		assertNotNull(response.getProducts());
		assertNotNull(response.getLastPage());
		assertNotNull(response.getPageNumber());
	}

	@Test
	public void deleteProductShouldDeleteSuccessfully(){
		var product = DataMocks.getProduct();
		var user = UserMock.getUser();
		user.setId(1L);
		product.setOwner(user);

		when(repository.existsById(any())).thenReturn(true);
		when(repository.findById(any())).thenReturn(Optional.of(product));

		service.deleteProduct(1L, 1L);
	}

	@Test
	public void deleteProductShouldThrowForbiddenExceptionWhenUserDoesNotOwnProduct(){
		var product = DataMocks.getProduct();
		var user = UserMock.getUser();
		user.setId(1L);
		product.setOwner(user);

		when(repository.existsById(any())).thenReturn(true);
		when(repository.findById(any())).thenReturn(Optional.of(product));

		assertThrows(ForbiddenException.class, ()->{
			service.deleteProduct(2L, 1L);
		});
	}

	@Test
	public void deleteProductShouldThrowNotFoundExceptionWhenProductIsNotFound(){
		var product = DataMocks.getProduct();
		var user = UserMock.getUser();
		user.setId(1L);
		product.setOwner(user);

		when(repository.existsById(any())).thenReturn(false);
		when(repository.findById(any())).thenReturn(Optional.of(product));

		assertThrows(NotFoundException.class, ()->{
			service.deleteProduct(1L, 1L);
		});
	}

}
