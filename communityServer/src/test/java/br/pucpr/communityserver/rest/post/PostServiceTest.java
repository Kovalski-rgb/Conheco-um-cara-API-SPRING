package br.pucpr.communityserver.rest.post;

import br.pucpr.communityserver.lib.exception.ForbiddenException;
import br.pucpr.communityserver.lib.exception.NotFoundException;
import br.pucpr.communityserver.rest.communities.CommunityRepository;
import br.pucpr.communityserver.rest.community.MockCommunities;
import br.pucpr.communityserver.rest.posts.PostRepository;
import br.pucpr.communityserver.rest.posts.PostService;
import br.pucpr.communityserver.rest.product.MockProducts;
import br.pucpr.communityserver.rest.products.ProductRepository;
import br.pucpr.communityserver.rest.service.MockServices;
import br.pucpr.communityserver.rest.services.ServiceRepository;
import br.pucpr.communityserver.rest.user.MockUsers;
import br.pucpr.communityserver.rest.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PostServiceTest {
    private PostRepository repository;
    private CommunityRepository communityRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private ServiceRepository serviceRepository;
    private PostService service;

    @BeforeEach
    public void setup() {
        repository = mock(PostRepository.class);
        communityRepository = mock(CommunityRepository.class);
        userRepository = mock(UserRepository.class);
        productRepository = mock(ProductRepository.class);
        serviceRepository = mock(ServiceRepository.class);
        service = new PostService(
                repository,
                communityRepository,
                userRepository,
                productRepository,
                serviceRepository
        );
    }

    // savePost
    @Test
    public void savePostShouldCreateANewPostWhenUserIsInsideCommunity(){
        var mockUser = MockUsers.getUser();
        var mockProduct = MockProducts.getProduct();
        var mockService = MockServices.getService();
        var mockCommunity = MockCommunities.getCommunity();
        var mockPost = MockPosts.getPost();

        var request = MockRequests.getPostRequest();
        request.setProductId(mockProduct.getId());
        request.setServiceId(mockService.getId());
        var userDTO = MockUsers.getUserTokenDTO();

        when(communityRepository.existsById(any())).thenReturn(true);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(mockUser);

        when(productRepository.existsById(any())).thenReturn(true);
        when(productRepository.save(any())).thenReturn(mockProduct);
        when(productRepository.findById(any())).thenReturn(Optional.of(mockProduct));

        when(serviceRepository.existsById(any())).thenReturn(true);
        when(serviceRepository.save(any())).thenReturn(mockService);
        when(serviceRepository.findById(any())).thenReturn(Optional.of(mockService));

        when(communityRepository.findById(any())).thenReturn(Optional.of(mockCommunity));
        when(repository.save(any())).thenReturn(mockPost);

        assertDoesNotThrow(() -> service.savePost(userDTO, request));
        verify(repository, times(1)).save(any());
    }

    @Test
    public void savePostShouldThrowNotFoundExceptionWhenCommunityIsNotFound(){
        var mockUser = MockUsers.getUser();
        var mockProduct = MockProducts.getProduct();
        var mockService = MockServices.getService();
        var mockCommunity = MockCommunities.getCommunity();
        var mockPost = MockPosts.getPost();

        var request = MockRequests.getPostRequest();
        request.setProductId(mockProduct.getId());
        request.setServiceId(mockService.getId());
        var userDTO = MockUsers.getUserTokenDTO();

        when(communityRepository.existsById(any())).thenReturn(false);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(mockUser);

        when(productRepository.existsById(any())).thenReturn(true);
        when(productRepository.save(any())).thenReturn(mockProduct);
        when(productRepository.findById(any())).thenReturn(Optional.of(mockProduct));

        when(serviceRepository.existsById(any())).thenReturn(true);
        when(serviceRepository.save(any())).thenReturn(mockService);
        when(serviceRepository.findById(any())).thenReturn(Optional.of(mockService));

        when(communityRepository.findById(any())).thenReturn(Optional.of(mockCommunity));
        when(repository.save(any())).thenReturn(mockPost);

        assertThrows(NotFoundException.class, () -> service.savePost(userDTO, request));
        verify(repository, times(0)).save(any());
    }

    @Test
    public void savePostShouldThrowForbiddenExceptionWhenUserIsNotInsideSpecifiedCommunity(){
        var mockUser = MockUsers.getUser();
        var mockProduct = MockProducts.getProduct();
        var mockService = MockServices.getService();
        var mockCommunity = MockCommunities.getCommunity();
        var mockPost = MockPosts.getPost();

        var request = MockRequests.getPostRequest();
        request.setProductId(mockProduct.getId());
        request.setServiceId(mockService.getId());
        var userDTO = MockUsers.getUserTokenDTO();

        when(communityRepository.existsById(any())).thenReturn(true);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(null);

        when(productRepository.existsById(any())).thenReturn(true);
        when(productRepository.save(any())).thenReturn(mockProduct);
        when(productRepository.findById(any())).thenReturn(Optional.of(mockProduct));

        when(serviceRepository.existsById(any())).thenReturn(true);
        when(serviceRepository.save(any())).thenReturn(mockService);
        when(serviceRepository.findById(any())).thenReturn(Optional.of(mockService));

        when(communityRepository.findById(any())).thenReturn(Optional.of(mockCommunity));
        when(repository.save(any())).thenReturn(mockPost);

        assertThrows(ForbiddenException.class, () -> service.savePost(userDTO, request));
        verify(repository, times(0)).save(any());
    }

}
