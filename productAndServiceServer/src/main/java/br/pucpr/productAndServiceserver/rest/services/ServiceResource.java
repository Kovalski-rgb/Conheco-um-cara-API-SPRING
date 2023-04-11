package br.pucpr.productAndServiceserver.rest.services;

import br.pucpr.productAndServiceserver.lib.security.JWT;
import br.pucpr.productAndServiceserver.rest.services.request.ServiceRequest;
import br.pucpr.productAndServiceserver.rest.services.request.UpdateServiceRequestDTO;
import br.pucpr.productAndServiceserver.rest.services.response.ServicePaginationResponse;
import br.pucpr.productAndServiceserver.rest.services.response.ServiceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service")
public class ServiceResource {

    private final JWT jwt;
    private final ServiceService service;

    public ServiceResource(JWT jwt, ServiceService service) {
        this.jwt = jwt;
        this.service = service;
    }


    @Operation(summary = "Request to register a new Service inside the system")
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


    @Operation(summary = "Gets user services, with pagination, the size of each page is 20")
    @GetMapping("/me/{page}")
    @Transactional
    @SecurityRequirement(name="JWT-token")
    @RolesAllowed("USER")
    public ResponseEntity<ServicePaginationResponse> getMyServices(
            HttpServletRequest headers,
            @Parameter(description = "Page number, begins at 0") @Min(0) @Valid @PathVariable Integer page
    ){
        var userDTO = jwt.decode(headers.getHeader("Authorization"));
        return ResponseEntity.ok(service.listFromUser(userDTO.getId(), page));
    }

    @Operation(summary = "Edits the specified service")
    @PutMapping("/{id}")
    @Transactional
    @SecurityRequirement(name="JWT-token")
    @RolesAllowed("USER")
    public ResponseEntity<ServiceResponse> update(
            HttpServletRequest headers,
            @Parameter(description = "The ID of the Service") @Valid @PathVariable Long id,
            @Valid @RequestBody UpdateServiceRequestDTO request
    ){
        var userDTO = jwt.decode(headers.getHeader("Authorization"));
        var result = service.updateService(userDTO.getId(), id, request);
        return result == null ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build() :
                ResponseEntity.ok(result);
    }

    @Operation(summary = "Deletes the specified service")
    @DeleteMapping("/{id}")
    @Transactional
    @SecurityRequirement(name="JWT-token")
    @RolesAllowed("USER")
    public void delete(
            HttpServletRequest headers,
            @Parameter(description = "The ID of the Service") @Valid @PathVariable Long id
    ){
        var userDTO = jwt.decode(headers.getHeader("Authorization"));
        service.deleteService(userDTO.getId(), id);
    }

}
