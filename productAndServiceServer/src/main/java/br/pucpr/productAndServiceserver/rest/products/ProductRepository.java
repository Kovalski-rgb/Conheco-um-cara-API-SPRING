package br.pucpr.productAndServiceserver.rest.products;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> getProductsByOwnerId(Long ownerId, PageRequest pageable);
    public List<Product> selectAllProducts(PageRequest pageable);
    public Integer countAllProducts();
    public Integer countProductsByOwnerId(Long ownerId);


}
