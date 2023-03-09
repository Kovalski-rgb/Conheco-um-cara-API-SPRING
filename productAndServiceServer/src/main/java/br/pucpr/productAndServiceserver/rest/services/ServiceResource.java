package br.pucpr.productAndServiceserver.rest.services;

import br.pucpr.productAndServiceserver.lib.security.JWT;
import br.pucpr.productAndServiceserver.rest.services.request.ServiceRequest;
import br.pucpr.productAndServiceserver.rest.services.response.ServiceResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service")
public class ServiceResource {

    private JWT jwt;
    private ServiceService service;

    public ServiceResource(JWT jwt, ServiceService service) {
        this.jwt = jwt;
        this.service = service;
    }

    @PostMapping("/register")
    @Transactional
    @SecurityRequirement(name="JWT-token")
    @RolesAllowed("USER")
    public ResponseEntity<Service> register(
            HttpServletRequest headers,
            @RequestBody @Valid ServiceRequest service
    ){
        var userDTO = jwt.decode(headers.getHeader("Authorization"));
        var response = this.service.register(userDTO, service);
        return response==null ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build() :
                ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Transactional
    @SecurityRequirement(name="JWT-token")
    @RolesAllowed("USER")
    public List<ServiceResponse> register(
            HttpServletRequest headers
    ){
        var userDTO = jwt.decode(headers.getHeader("Authorization"));
        return service.listFromUser(userDTO.getId());
    }

    @PutMapping
    @Transactional
    @SecurityRequirement(name="JWT-token")
    @RolesAllowed("USER")
    public ResponseEntity<ServiceResponse> update(
            HttpServletRequest headers,
            @Valid @RequestParam Long id,
            @Valid @RequestBody ServiceRequest request
    ){
        var userDTO = jwt.decode(headers.getHeader("Authorization"));
        var result = service.updateService(userDTO.getId(), id, request);
        return result == null ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build() :
                ResponseEntity.ok(result);
    }

    @DeleteMapping
    @Transactional
    @SecurityRequirement(name="JWT-token")
    @RolesAllowed("USER")
    public void delete(
            HttpServletRequest headers,
            @Valid @RequestParam Long id
    ){
        var userDTO = jwt.decode(headers.getHeader("Authorization"));
        service.deleteService(userDTO.getId(), id);
    }

}
