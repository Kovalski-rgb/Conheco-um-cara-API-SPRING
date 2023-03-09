package br.pucpr.productAndServiceserver.rest.products;

import br.pucpr.productAndServiceserver.lib.exception.ForbiddenException;
import br.pucpr.productAndServiceserver.lib.exception.NotFoundException;
import br.pucpr.productAndServiceserver.rest.products.request.ProductRequest;
import br.pucpr.productAndServiceserver.rest.products.response.ProductResponse;
import br.pucpr.productAndServiceserver.rest.users.User;
import br.pucpr.productAndServiceserver.rest.users.UserRepository;
import br.pucpr.productAndServiceserver.rest.users.requests.UserTokenDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    private ProductRepository repository;
    private UserRepository userRepository;

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

    public List<ProductResponse> listFromUser(Long userId) {
//        if(!userRepository.existsById(userId)) throw new NotFoundException("User not found");
        return repository.getProductsByOwnerId(userId).stream().map(ProductResponse::new).toList();
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
