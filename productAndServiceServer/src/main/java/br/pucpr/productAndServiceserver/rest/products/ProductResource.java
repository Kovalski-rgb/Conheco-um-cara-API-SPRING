package br.pucpr.productAndServiceserver.rest.products;

import br.pucpr.productAndServiceserver.lib.security.JWT;
import br.pucpr.productAndServiceserver.rest.products.request.ProductRequest;
import br.pucpr.productAndServiceserver.rest.products.response.AdminPaginationResponse;
import br.pucpr.productAndServiceserver.rest.products.response.ProductPaginationResponse;
import br.pucpr.productAndServiceserver.rest.products.response.ProductResponse;
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
@RequestMapping("/product")
public class ProductResource {

    private final JWT jwt;
    private final ProductService service;

    public ProductResource(JWT jwt, ProductService service) {
        this.jwt = jwt;
        this.service = service;
    }
// TODO partial implementation of admin CRUD routes
//    @GetMapping("/user/{userId}/{page}")
//    @Transactional
//    @SecurityRequirement(name="JWT-token")
//    @RolesAllowed("ADMIN")
//    public ResponseEntity<ProductPaginationResponse> getPaginatedUserProducts(
//            @Valid @PathVariable Long userId,
//            @Valid @PathVariable Integer page
//    ){
//        return ResponseEntity.ok(service.listFromUser(userId, page));
//    }
//
//    @GetMapping("/all/{page}")
//    @Transactional
//    @SecurityRequirement(name="JWT-token")
//    @RolesAllowed("ADMIN")
//    public ResponseEntity<AdminPaginationResponse> getPaginatedAllProducts(
//            @Valid @PathVariable Integer page
//    ){
//        return ResponseEntity.ok(service.listAllProducts(page));
//    }

    @PostMapping("/register")
    @Transactional
    @SecurityRequirement(name="JWT-token")
    @RolesAllowed("USER")
    public ResponseEntity<Product> register(
            HttpServletRequest headers,
            @RequestBody @Valid ProductRequest product
    ){
        var userDTO = jwt.decode(headers.getHeader("Authorization"));
        var response = service.register(userDTO, product);
        return response==null ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build() :
                ResponseEntity.ok(response);
    }

    @GetMapping("/me/{page}")
    @Transactional
    @SecurityRequirement(name="JWT-token")
    @RolesAllowed("USER")
    public ResponseEntity<ProductPaginationResponse> getMyProducts(
            HttpServletRequest headers,
            @Valid @PathVariable Integer page
    ){
        var userDTO = jwt.decode(headers.getHeader("Authorization"));
        return ResponseEntity.ok(service.listFromUser(userDTO.getId(), page));
    }

    @PutMapping("/{id}")
    @Transactional
    @SecurityRequirement(name="JWT-token")
    @RolesAllowed({"USER"})
    public ResponseEntity<ProductResponse> update(
            HttpServletRequest headers,
            @Valid @PathVariable Long id,
            @Valid @RequestBody ProductRequest request
    ){
        var userDTO = jwt.decode(headers.getHeader("Authorization"));
        var result = service.updateProduct(userDTO.getId(), id, request);
        return result == null ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build() :
                ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @SecurityRequirement(name="JWT-token")
    @RolesAllowed({"USER", "ADMIN"})
    public void delete(
            HttpServletRequest headers,
            @Valid @PathVariable Long id
    ){
        var userDTO = jwt.decode(headers.getHeader("Authorization"));
        service.deleteProduct(userDTO.getId(), id);
    }

}
