package br.pucpr.productAndServiceserver.rest.services;

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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServiceServiceTest {


    private ServiceRepository repository;
    private UserRepository userRepository;
    private ServiceService service;

    @BeforeEach
    public void setup() {
        repository = mock(ServiceRepository.class);
        userRepository = mock(UserRepository.class);
        service = new ServiceService(repository, userRepository);
    }

    @Test
    public void registerShouldSaveAServiceSuccessfullyWithExistingUser() {
        when(userRepository.existsById(any())).thenReturn(true);
        when(repository.save(any())).thenReturn(ServiceMocks.getService());
        var result = service.register(UserMock.getUserTokenDTO(), RequestMocks.getServiceRequest());

        assertNotNull(result.getOwner());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    public void registerShouldSaveAServiceSuccessfullyWithNewUser() {
        when(userRepository.existsById(any())).thenReturn(false);
        when(repository.save(any())).thenReturn(ServiceMocks.getService());
        when(userRepository.save(any())).thenReturn(UserMock.getUser());
        var result = service.register(UserMock.getUserTokenDTO(), RequestMocks.getServiceRequest());

        assertNotNull(result.getOwner());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    public void listAllShouldReturnSomeServices(){
        var list = Stream.of(ServiceMocks.getService(), ServiceMocks.getService(), ServiceMocks.getService()).collect(Collectors.toList());
        when(repository.selectAllServices(any())).thenReturn(list);
        when(repository.countAllServices()).thenReturn(list.size());
        var response = service.listAllServices(0);

        assertNotNull(response.getServices());
        assertNotNull(response.getLastPage());
        assertNotNull(response.getPageNumber());
    }

    @Test
    public void listAllShouldNotActWeirdWhenThereAreNoServices(){
        when(repository.selectAllServices(any())).thenReturn(new ArrayList<>());
        when(repository.countAllServices()).thenReturn(0);
        var response = service.listAllServices(0);

        assertNotNull(response.getServices());
        assertNotNull(response.getLastPage());
        assertNotNull(response.getPageNumber());
    }

    @Test
    public void listFromUserShouldReturnUserServices(){
        when(userRepository.existsById(any())).thenReturn(true);
        var list = Stream.of(ServiceMocks.getService(), ServiceMocks.getService()).collect(Collectors.toList());
        when(repository.getServicesByOwnerId(any(), any())).thenReturn(list);
        when(repository.countServicesByOwnerId(any())).thenReturn(list.size());

        var response = service.listFromUser(1L, 0);
        assertNotNull(response.getServices());
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
    public void listFromUserShouldNotActWeirdWhenThereAreNoServices(){
        when(userRepository.existsById(any())).thenReturn(true);
        when(repository.getServicesByOwnerId(any(), any())).thenReturn(new ArrayList<>());
        when(repository.countServicesByOwnerId(any())).thenReturn(0);

        var response = service.listFromUser(1L, 0);
        assertNotNull(response.getServices());
        assertNotNull(response.getLastPage());
        assertNotNull(response.getPageNumber());
    }

    @Test
    public void deleteServiceShouldDeleteSuccessfully(){
        var mockService = ServiceMocks.getService();
        var user = UserMock.getUser();
        user.setId(1L);
        mockService.setOwner(user);

        when(repository.existsById(any())).thenReturn(true);
        when(repository.findById(any())).thenReturn(Optional.of(mockService));

        service.deleteService(1L, 1L);
    }

    @Test
    public void deleteServiceShouldThrowForbiddenExceptionWhenUserDoesNotOwnService(){
        var mockService = ServiceMocks.getService();
        var user = UserMock.getUser();
        user.setId(1L);
        mockService.setOwner(user);

        when(repository.existsById(any())).thenReturn(true);
        when(repository.findById(any())).thenReturn(Optional.of(mockService));

        assertThrows(ForbiddenException.class, ()->{
            service.deleteService(2L, 1L);
        });
    }

    @Test
    public void deleteServiceShouldThrowNotFoundExceptionWhenServiceIsNotFound(){
        var mockService = ServiceMocks.getService();
        var user = UserMock.getUser();
        user.setId(1L);
        mockService.setOwner(user);

        when(repository.existsById(any())).thenReturn(false);
        when(repository.findById(any())).thenReturn(Optional.of(mockService));

        assertThrows(NotFoundException.class, ()->{
            service.deleteService(1L, 1L);
        });
    }

    @Test
    public void updateServiceShouldUpdateSuccessfully(){
        var mockService = ServiceMocks.getService();
        var user = UserMock.getUser();
        user.setId(1L);
        mockService.setOwner(user);
        var request = RequestMocks.getUpdateServiceRequestDTO();

        when(repository.existsById(any())).thenReturn(true);
        when(repository.findById(any())).thenReturn(Optional.of(mockService));
        when(repository.save(any())).thenReturn(mockService);

        var result = service.updateService(1L, 1L, request);

        assertEquals(result.getName(), request.getName());
        assertEquals(result.getPrice(), request.getPrice());
        assertEquals(result.getDescription(), request.getDescription());
    }

    @Test
    public void updateServiceShouldThrowForbiddenExceptionWhenUserDoesNotOwnService(){
        var mockService = ServiceMocks.getService();
        var user = UserMock.getUser();
        user.setId(1L);
        mockService.setOwner(user);
        var request = RequestMocks.getUpdateServiceRequestDTO();

        when(repository.existsById(any())).thenReturn(true);
        when(repository.findById(any())).thenReturn(Optional.of(mockService));

        assertThrows(ForbiddenException.class, ()->{
            service.updateService(2L, 1L, request);
        });
    }

    @Test
    public void updateServiceShouldThrowNotFoundExceptionWhenServiceIsNotFound(){
        var mockService = ServiceMocks.getService();
        var user = UserMock.getUser();
        user.setId(1L);
        mockService.setOwner(user);
        var request = RequestMocks.getUpdateServiceRequestDTO();

        when(repository.existsById(any())).thenReturn(false);
        when(repository.findById(any())).thenReturn(Optional.of(mockService));

        assertThrows(NotFoundException.class, ()->{
            service.updateService(1L, 1L, request);
        });
    }


}
