package br.pucpr.productAndServiceserver.rest.products;

import br.pucpr.productAndServiceserver.rest.products.request.ProductRequest;
import br.pucpr.productAndServiceserver.rest.products.request.UpdateProductRequestDTO;
import br.pucpr.productAndServiceserver.rest.users.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;


@NamedQuery(
        name="Product.getProductsByOwnerId",
        query = "SELECT p from Product p" +
                " JOIN p.owner o" +
                " WHERE o.id = :ownerId"
)
@NamedQuery(
        name="Product.countAllProducts",
        query = "SELECT COUNT(p) FROM Product p"
)
@NamedQuery(
        name="Product.countProductsByOwnerId",
        query = "SELECT COUNT(p) FROM Product p" +
                " JOIN p.owner o" +
                " WHERE o.id = :ownerId"
)
@NamedQuery(
        name="Product.selectAllProducts",
        query = "SELECT p from Product p"
)

@Entity @Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Schema(example = "1")
    @Id @GeneratedValue
    private Long id;

    @Schema(example = "Product name")
    @NotEmpty
    private String name;

    @Schema(example = "Product description")
    private String description;

    @NotNull
    @ManyToOne
    private User owner;

    @Schema(example = "12.34")
    @NotNull
    private Double price;

//    @Schema(example = "Headphones")
//    @NotEmpty
//    @ElementCollection
//    @CollectionTable
//            (name = "ProductType", joinColumns = @JoinColumn(name = "id"))
//    @Column(name = "productType")
//    private Set<String> productType;

    @Schema(example = "2023-04-09 15:53:23.000")
    private LocalDateTime createdAt;

    public Product fromRequest(ProductRequest request){
        this.name = request.getName();
        this.description = request.getDescription();
//        this.productType = request.getProductType();
        this.price = request.getPrice();
        return this;
    }

    public void update(UpdateProductRequestDTO product){
        if(product.getName() != null && !product.getName().isEmpty()) this.name = product.getName();
        if(product.getDescription() != null) this.description = product.getDescription();
        if(product.getPrice() != null  && !product.getPrice().isNaN())this.price = product.getPrice();
//        if(product.getProductType() != null) this.productType = product.getProductType();
    }

}
