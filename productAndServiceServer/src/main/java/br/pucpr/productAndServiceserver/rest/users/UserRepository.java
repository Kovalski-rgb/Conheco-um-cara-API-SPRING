package br.pucpr.productAndServiceserver.rest.users;

import br.pucpr.productAndServiceserver.rest.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


}
