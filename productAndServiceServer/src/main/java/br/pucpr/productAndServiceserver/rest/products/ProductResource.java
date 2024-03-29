package br.pucpr.productAndServiceserver.rest.products;

import br.pucpr.productAndServiceserver.lib.security.JWT;
import br.pucpr.productAndServiceserver.rest.products.request.ProductRequest;
import br.pucpr.productAndServiceserver.rest.products.request.UpdateProductRequestDTO;
import br.pucpr.productAndServiceserver.rest.products.response.ProductPaginationResponse;
import br.pucpr.productAndServiceserver.rest.products.response.ProductResponse;
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

    @Operation(summary = "Request to register a new Product inside the system")
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

    @Operation(summary = "Gets user products, with pagination, the size of each page is 20")
    @GetMapping("/me/{page}")
    @Transactional
    @SecurityRequirement(name="JWT-token")
    @RolesAllowed("USER")
    public ResponseEntity<ProductPaginationResponse> getMyProducts(
            HttpServletRequest headers,
            @Parameter(description = "Page number, begins at 0") @Min(0) @Valid @PathVariable Integer page
    ){
        var userDTO = jwt.decode(headers.getHeader("Authorization"));
        return ResponseEntity.ok(service.listFromUser(userDTO.getId(), page));
    }

    @Operation(summary = "Edits the specified product")
    @PutMapping("/{id}")
    @Transactional
    @SecurityRequirement(name="JWT-token")
    @RolesAllowed({"USER"})
    public ResponseEntity<ProductResponse> update(
            HttpServletRequest headers,
            @Parameter(description = "The ID of the Product") @Valid @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequestDTO request
    ){
        var userDTO = jwt.decode(headers.getHeader("Authorization"));
        var result = service.updateProduct(userDTO.getId(), id, request);
        return result == null ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build() :
                ResponseEntity.ok(result);
    }

    @Operation(summary = "Deletes the specified product")
    @DeleteMapping("/{id}")
    @Transactional
    @SecurityRequirement(name="JWT-token")
    @RolesAllowed({"USER", "ADMIN"})
    public void delete(
            HttpServletRequest headers,
            @Parameter(description = "The ID of the Product") @Valid @PathVariable Long id
    ){
        var userDTO = jwt.decode(headers.getHeader("Authorization"));
        service.deleteProduct(userDTO.getId(), id);
    }

}
