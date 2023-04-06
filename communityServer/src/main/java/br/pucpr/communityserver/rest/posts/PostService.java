package br.pucpr.communityserver.rest.posts;

import br.pucpr.communityserver.lib.exception.ForbiddenException;
import br.pucpr.communityserver.lib.exception.NotFoundException;
import br.pucpr.communityserver.rest.communities.CommunityRepository;
import br.pucpr.communityserver.rest.posts.requests.EditPostRequest;
import br.pucpr.communityserver.rest.posts.requests.PostRequest;
import br.pucpr.communityserver.rest.posts.response.PostResponse;
import br.pucpr.communityserver.rest.products.Product;
import br.pucpr.communityserver.rest.products.ProductRepository;
import br.pucpr.communityserver.rest.services.Service;
import br.pucpr.communityserver.rest.services.ServiceRepository;
import br.pucpr.communityserver.rest.users.User;
import br.pucpr.communityserver.rest.users.UserRepository;
import br.pucpr.communityserver.rest.users.requests.UserTokenDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class PostService {

    PostRepository repository;
    CommunityRepository communityRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    ServiceRepository serviceRepository;

    public PostService(PostRepository repository,
                       CommunityRepository communityRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository,
                       ServiceRepository serviceRepository) {
        this.repository = repository;
        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
    }

    public Post savePost(UserTokenDTO userTokenDTO, PostRequest request){
        if(!communityRepository.existsById(request.getCommunityId())) throw new NotFoundException("Community not found");
        if(communityRepository.getUserInCommunityById(request.getCommunityId(), userTokenDTO.getId()) == null)
            throw new ForbiddenException("User must be inside specified community to create a post");
        var product = (!productRepository.existsById(request.getProductId())) ?
                productRepository.save(new Product(request.getProductId(), new User(userTokenDTO))) :
                productRepository.findById(request.getProductId()).get();
        var service = (!serviceRepository.existsById(request.getServiceId())) ?
                serviceRepository.save(new Service(request.getServiceId(), new User(userTokenDTO))) :
                serviceRepository.findById(request.getServiceId()).get();

        var newPost = new Post(request);
        newPost.setService(service);
        newPost.setProduct(product);
        newPost.setCommunity(communityRepository.findById(request.getCommunityId()).get());
        newPost.setCreator(new User(userTokenDTO));
        newPost.setCreatedAt(LocalDateTime.now());

        return repository.save(newPost);
    }

    public List<PostResponse> getAllPostsFromUser(UserTokenDTO userTokenDTO){
        var queryResult = repository.getAllPostsFromUserById(userTokenDTO.getId());
        return queryResult.stream().map(PostResponse::new).collect(Collectors.toList());
    }

    public void deletePost(Long postId, Long communityId, UserTokenDTO userTokenDTO){
        if(!communityRepository.existsById(communityId)) throw new NotFoundException("Community not found");
        if(communityRepository.getUserInCommunityById(communityId, userTokenDTO.getId()) == null)
            throw new ForbiddenException("User must be inside specified community to delete a post");
        if(!repository.existsById(postId)) throw new NotFoundException("Post not found");
        if(!repository.findById(postId).get().getCreator().getId().equals(userTokenDTO.getId())) {
            if (communityRepository.getModeratorByCommunityAndUser(communityId, userTokenDTO.getId()) == null)
                throw new ForbiddenException("User that did not create the post cannot delete it, without moderator permissions");
        }
        repository.deletePostById(postId);
    }

    public List<PostResponse> getAllPostsFromCommunityFromUser(Long communityId, UserTokenDTO userTokenDTO){
        if(!communityRepository.existsById(communityId)) throw new NotFoundException("Community not found");
        if(communityRepository.getUserInCommunityById(communityId, userTokenDTO.getId()) == null)
            throw new ForbiddenException("User does not belong to specified community");
        var queryResult = repository.getAllPostsFromUserByCommunityById(communityId, userTokenDTO.getId());
        return queryResult.stream().map(PostResponse::new).collect(Collectors.toList());
    }

    public List<PostResponse> getAllPostsFromCommunity(Long communityId, UserTokenDTO userTokenDTO){
        if(!communityRepository.existsById(communityId)) throw new NotFoundException("Community not found");
        if(communityRepository.getUserInCommunityById(communityId, userTokenDTO.getId()) == null)
            throw new ForbiddenException("User does not belong to specified community");
        var queryResult = repository.getAllPostsFromCommunityById(communityId);
        return queryResult.stream().map(PostResponse::new).collect(Collectors.toList());
    }

    public PostResponse editPost(EditPostRequest request, Long postId, Long communityId, UserTokenDTO userTokenDTO){
        if(!communityRepository.existsById(communityId)) throw new NotFoundException("Community not found");
        if(communityRepository.getUserInCommunityById(communityId, userTokenDTO.getId()) == null)
            throw new ForbiddenException("User must be inside specified community to delete a post");
        if(!repository.existsById(postId)) throw new NotFoundException("Post not found");
        if(!repository.findById(postId).get().getCreator().getId().equals(userTokenDTO.getId())) {
            if (communityRepository.getModeratorByCommunityAndUser(communityId, userTokenDTO.getId()) == null)
                throw new ForbiddenException("User that did not create the post cannot edit it, without moderator permissions");
        }

        var product = (!productRepository.existsById(request.getProductId())) ?
                productRepository.save(new Product(request.getProductId(), new User(userTokenDTO))) :
                productRepository.findById(request.getProductId()).get();
        var service = (!serviceRepository.existsById(request.getServiceId())) ?
                serviceRepository.save(new Service(request.getServiceId(), new User(userTokenDTO))) :
                serviceRepository.findById(request.getServiceId()).get();

        var post = repository.findById(postId).get();
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setService(service);
        post.setProduct(product);
        post.setUpdatedAt(LocalDateTime.now());

        repository.save(post);
        return new PostResponse(post);
    }

}
