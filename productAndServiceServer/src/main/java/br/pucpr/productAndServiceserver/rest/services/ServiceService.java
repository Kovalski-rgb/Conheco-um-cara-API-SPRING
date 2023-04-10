package br.pucpr.productAndServiceserver.rest.services;

import br.pucpr.productAndServiceserver.lib.exception.ForbiddenException;
import br.pucpr.productAndServiceserver.lib.exception.NotFoundException;
import br.pucpr.productAndServiceserver.rest.services.request.ServiceRequest;
import br.pucpr.productAndServiceserver.rest.services.request.UpdateServiceRequestDTO;
import br.pucpr.productAndServiceserver.rest.services.response.AdminServicePaginationResponse;
import br.pucpr.productAndServiceserver.rest.services.response.ServicePaginationResponse;
import br.pucpr.productAndServiceserver.rest.services.response.ServiceResponse;
import br.pucpr.productAndServiceserver.rest.users.User;
import br.pucpr.productAndServiceserver.rest.users.UserRepository;
import br.pucpr.productAndServiceserver.rest.users.requests.UserTokenDTO;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

@org.springframework.stereotype.Service
public class ServiceService {
    private final ServiceRepository repository;
    private final UserRepository userRepository;
    private final Integer pageSize = 20;

    public ServiceService(ServiceRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Service register(UserTokenDTO userData, ServiceRequest serviceRequest){
        var user = new User();
        if(!userRepository.existsById(userData.getId())) {
            user = userRepository.save(new User().fromUserTokenDTO(userData));
        } else {
            user.fromUserTokenDTO(userData);
        }
        var service = new Service().fromRequest(serviceRequest);
        service.setCreatedAt(LocalDateTime.now());
        service.setOwner(user);
        return repository.save(service);
    }

    public AdminServicePaginationResponse listAllServices(Integer page){
        var response = new AdminServicePaginationResponse();
        response.setServices(repository.selectAllServices(PageRequest.of(page, pageSize)));
        response.setPageNumber(page);
        response.setLastPage(repository.countAllServices()/pageSize);
        return response;
    }

    public ServicePaginationResponse listFromUser(Long userId, Integer page) {
        if(!userRepository.existsById(userId)) throw new NotFoundException("User not found");
        var response = new ServicePaginationResponse();
        response.setServices(repository.getServicesByOwnerId(userId, PageRequest.of(page, pageSize))
                .stream().map(ServiceResponse::new).toList()
        );
        response.setPageNumber(page);
        response.setLastPage(repository.countServicesByOwnerId(userId)/pageSize);
        return response;
    }

    public ServiceResponse updateService(Long userId, Long serviceId, UpdateServiceRequestDTO request){
        if(!repository.existsById(serviceId)) throw new NotFoundException("Service not found");
        var service = repository.findById(serviceId).get();
        if(!service.getOwner().getId().equals(userId)) throw new ForbiddenException("This service is not from the current logged user");
        service.update(request);
        return new ServiceResponse(repository.save(service));
    }

    public void deleteService(Long userId, Long serviceId){
        if(!repository.existsById(serviceId)) throw new NotFoundException("Service not found");
        var service = repository.findById(serviceId).get();
        if(!service.getOwner().getId().equals(userId)) throw new ForbiddenException("This service is not from the current logged user");
        repository.deleteById(service.getId());

    }

}
