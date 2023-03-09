package br.pucpr.productAndServiceserver.rest.products;

import br.pucpr.productAndServiceserver.lib.security.JWT;
import br.pucpr.productAndServiceserver.rest.products.request.ProductRegisterRequest;
import br.pucpr.productAndServiceserver.rest.products.request.ProductUpdateRequest;
import br.pucpr.productAndServiceserver.rest.products.response.ProductResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductResource {

    private JWT jwt;
    private ProductService service;

    public ProductResource(JWT jwt, ProductService service) {
        this.jwt = jwt;
        this.service = service;
    }

    @PostMapping("/register")
    @Transactional
    @SecurityRequirement(name="JWT-token")
    @RolesAllowed("USER")
    public ResponseEntity<Product> register(
            HttpServletRequest headers,
            @RequestBody @Valid ProductRegisterRequest product
    ){
        var userDTO = jwt.decode(headers.getHeader("Authorization"));
        var response = service.register(userDTO, product);
        return response==null ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build() :
                ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Transactional
    @SecurityRequirement(name="JWT-token")
    @RolesAllowed("USER")
    public List<ProductResponse> register(
            HttpServletRequest headers
    ){
        var userDTO = jwt.decode(headers.getHeader("Authorization"));
        return service.listFromUser(userDTO.getId());
    }

    @PutMapping
    @Transactional
    @SecurityRequirement(name="JWT-token")
    @RolesAllowed("USER")
    public ResponseEntity<ProductResponse> update(
            HttpServletRequest headers,
            @Valid @RequestParam Long id,
            @Valid @RequestBody ProductUpdateRequest request
    ){
        var userDTO = jwt.decode(headers.getHeader("Authorization"));
        var result = service.updateProduct(userDTO.getId(), id, request);
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
        service.deleteProduct(userDTO.getId(), id);
    }
    //TODO delete by id (only from user)

}
