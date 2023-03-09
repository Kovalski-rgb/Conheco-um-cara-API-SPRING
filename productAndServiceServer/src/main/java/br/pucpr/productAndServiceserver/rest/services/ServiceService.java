package br.pucpr.productAndServiceserver.rest.services;

import br.pucpr.productAndServiceserver.lib.exception.ForbiddenException;
import br.pucpr.productAndServiceserver.lib.exception.NotFoundException;
import br.pucpr.productAndServiceserver.rest.services.request.ServiceRequest;
import br.pucpr.productAndServiceserver.rest.services.response.ServiceResponse;
import br.pucpr.productAndServiceserver.rest.users.User;
import br.pucpr.productAndServiceserver.rest.users.UserRepository;
import br.pucpr.productAndServiceserver.rest.users.requests.UserTokenDTO;

import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {

    private ServiceRepository repository;
    private UserRepository userRepository;

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

    public List<ServiceResponse> listFromUser(Long userId) {
//        if(!userRepository.existsById(userId)) throw new NotFoundException("User not found");
        return repository.getServicesByOwnerId(userId).stream().map(ServiceResponse::new).toList();
    }

    public ServiceResponse updateService(Long userId, Long serviceId, ServiceRequest request){
        if(!repository.existsById(serviceId)) throw new NotFoundException("Service not found");
        var service = repository.findById(serviceId).get();
        if(service.getOwner().getId().intValue() != userId.intValue()) throw new ForbiddenException("This service is not from the current logged user");
        service.update(request);
        return new ServiceResponse(repository.save(service));
    }

    public void deleteService(Long userId, Long serviceId){
        if(!repository.existsById(serviceId)) throw new NotFoundException("Service not found");
        var service = repository.findById(serviceId).get();
        if(service.getOwner().getId().intValue() != userId.intValue()) throw new ForbiddenException("This service is not from the current logged user");
        repository.deleteById(service.getId());

    }

}
