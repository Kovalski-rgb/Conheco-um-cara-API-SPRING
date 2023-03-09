package br.pucpr.productAndServiceserver.rest.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> getProductsByOwnerId(Long ownerId);


}
