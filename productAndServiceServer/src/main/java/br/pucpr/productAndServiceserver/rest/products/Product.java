package br.pucpr.productAndServiceserver.rest.products;

import br.pucpr.productAndServiceserver.rest.products.request.ProductRequest;
import br.pucpr.productAndServiceserver.rest.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;


@NamedQuery(
        name="Product.getProductsByCreatorId",
        query = "SELECT p from Product p" +
                " JOIN p.owner o" +
                " WHERE o.id = :creatorId"
)

@Entity @Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id @GeneratedValue
    private Long id;

    @NotEmpty
    private String name;

    private String description;

    @NotNull
    @ManyToOne
    private User owner;

    @NotNull
    private Double price;

    @NotEmpty
    @ElementCollection
    @CollectionTable
            (name = "ProductType", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "productType")
    private Set<String> productType;

    private LocalDateTime createdAt;

    public Product fromRequest(ProductRequest request){
        this.name = request.getName();
        this.description = request.getDescription();
        this.productType = request.getProductType();
        this.price = request.getPrice();
        return this;
    }

    public void update(ProductRequest product){
        if(product.getName() != null && !product.getName().isEmpty()) this.name = product.getName();
        if(product.getDescription() != null) this.description = product.getDescription();
        if(product.getPrice() != null  && !product.getPrice().isNaN())this.price = product.getPrice();
        if(product.getProductType() != null) this.productType = product.getProductType();
    }

}
