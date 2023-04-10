package br.pucpr.productAndServiceserver.rest.products;

import br.pucpr.productAndServiceserver.lib.exception.ForbiddenException;
import br.pucpr.productAndServiceserver.lib.exception.NotFoundException;
import br.pucpr.productAndServiceserver.rest.products.request.ProductRequest;
import br.pucpr.productAndServiceserver.rest.products.response.AdminPaginationResponse;
import br.pucpr.productAndServiceserver.rest.products.response.ProductPaginationResponse;
import br.pucpr.productAndServiceserver.rest.products.response.ProductResponse;
import br.pucpr.productAndServiceserver.rest.users.User;
import br.pucpr.productAndServiceserver.rest.users.UserRepository;
import br.pucpr.productAndServiceserver.rest.users.requests.UserTokenDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final UserRepository userRepository;

    private final Integer pageSize = 20;

    public ProductService(ProductRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Product register(UserTokenDTO userData, ProductRequest productRequest){
        var user = new User();
        if(!userRepository.existsById(userData.getId())) {
            user = userRepository.save(new User().fromUserTokenDTO(userData));
        } else {
            user.fromUserTokenDTO(userData);
        }
        var product = new Product().fromRequest(productRequest);
        product.setCreatedAt(LocalDateTime.now());
        product.setOwner(user);
        return repository.save(product);
    }

    public AdminPaginationResponse listAllProducts(Integer page){
        var response = new AdminPaginationResponse();
        response.setProducts(repository.selectAllProducts(PageRequest.of(page, pageSize)));
        response.setPageNumber(page);
        response.setLastPage(repository.countAllProducts()/pageSize);
        return response;
    }

    public ProductPaginationResponse listFromUser(Long userId, Integer page) {
        if(!userRepository.existsById(userId)) throw new NotFoundException("User not found");
        var response = new ProductPaginationResponse();
        response.setProducts(repository.getProductsByOwnerId(userId, PageRequest.of(page, pageSize))
                .stream().map(ProductResponse::new).toList()
        );
        response.setPageNumber(page);
        response.setLastPage(repository.countProductsByOwnerId(userId)/pageSize);
        return response;
    }

    public ProductResponse updateProduct(Long userId, Long productId, ProductRequest request){
        if(!repository.existsById(productId)) throw new NotFoundException("Product not found");
        var product = repository.findById(productId).get();
        if(product.getOwner().getId().intValue() != userId.intValue()) throw new ForbiddenException("This product is not from the current logged user");
        product.update(request);
        return new ProductResponse(repository.save(product));
    }

    public void deleteProduct(Long userId, Long productId){
        if(!repository.existsById(productId)) throw new NotFoundException("Product not found");
        var product = repository.findById(productId).get();
        if(product.getOwner().getId().intValue() != userId.intValue()) throw new ForbiddenException("This product is not from the current logged user");
        repository.deleteById(product.getId());

    }

}
