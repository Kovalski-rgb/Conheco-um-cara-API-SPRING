package br.pucpr.communityserver.rest.post;

import br.pucpr.communityserver.lib.exception.ForbiddenException;
import br.pucpr.communityserver.lib.exception.NotFoundException;
import br.pucpr.communityserver.rest.communities.CommunityRepository;
import br.pucpr.communityserver.rest.community.MockCommunities;
import br.pucpr.communityserver.rest.posts.Post;
import br.pucpr.communityserver.rest.posts.PostRepository;
import br.pucpr.communityserver.rest.posts.PostService;
import br.pucpr.communityserver.rest.posts.response.PostResponse;
import br.pucpr.communityserver.rest.product.MockProducts;
import br.pucpr.communityserver.rest.products.ProductRepository;
import br.pucpr.communityserver.rest.service.MockServices;
import br.pucpr.communityserver.rest.services.ServiceRepository;
import br.pucpr.communityserver.rest.user.MockUsers;
import br.pucpr.communityserver.rest.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    //    getAllPostsFromUser
    @Test
    public void getAllPostsFromUserShouldReturnAllPostsFromUser(){
        var userDTO = MockUsers.getUserTokenDTO();
        var postList = Stream.of(MockPosts.getPost(), MockPosts.getPost(), MockPosts.getPost()).collect(Collectors.toList());
        when(repository.getAllPostsFromUserById(any())).thenReturn(postList);

        assertDoesNotThrow(()->{
            var result = service.getAllPostsFromUser(userDTO);
            assertNotNull(result);
        });
    }

    @Test
    public void getAllPostsFromUserShouldReturnAllPostsFromUserEvenWhenThereAreNoPostsYet(){
        var userDTO = MockUsers.getUserTokenDTO();
        var postList = new ArrayList<Post>();
        when(repository.getAllPostsFromUserById(any())).thenReturn(postList);

        assertDoesNotThrow(()->{
            var result = service.getAllPostsFromUser(userDTO);
            assertNotNull(result);
        });
    }

    // deletePost
    @Test
    public void deletePostShouldDeletePostOfItsCreatorThatDoesNotHaveModeratorPrivileges(){
        var mockUser = MockUsers.getUser();
        var mockPost = MockPosts.getPost();
        var userDTO = MockUsers.getUserTokenDTO();

        when(communityRepository.existsById(any())).thenReturn(true);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(mockUser);

        when(repository.existsById(any())).thenReturn(true);
        when(repository.findById(any())).thenReturn(Optional.of(mockPost));

        when(communityRepository.getModeratorByCommunityAndUser(any(), any())).thenReturn(null);

        assertDoesNotThrow(()-> service.deletePost(mockPost.getId(), mockPost.getCommunity().getId(), userDTO));
        verify(repository, times(1)).deletePostById(any());
    }

    @Test
    public void deletePostShouldDeletePostOfAnotherUserOnlyWhenTheRequesterHadModeratorPrivileges(){
        var mockUser = MockUsers.getUser();
        var mockPost = MockPosts.getPost();
        var userDTO = MockUsers.getUserTokenDTO();
        userDTO.setId(2L);

        when(communityRepository.existsById(any())).thenReturn(true);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(mockUser);

        when(repository.existsById(any())).thenReturn(true);
        when(repository.findById(any())).thenReturn(Optional.of(mockPost));

        when(communityRepository.getModeratorByCommunityAndUser(any(), any())).thenReturn(mockUser);

        assertDoesNotThrow(()-> service.deletePost(mockPost.getId(), mockPost.getCommunity().getId(), userDTO));
        verify(repository, times(1)).deletePostById(any());
    }

    @Test
    public void deletePostShouldThrowNotFoundExceptionWhenCommunityWasNotFound(){
        var mockUser = MockUsers.getUser();
        var mockPost = MockPosts.getPost();
        var userDTO = MockUsers.getUserTokenDTO();

        when(communityRepository.existsById(any())).thenReturn(false);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(mockUser);

        when(repository.existsById(any())).thenReturn(true);
        when(repository.findById(any())).thenReturn(Optional.of(mockPost));

        when(communityRepository.getModeratorByCommunityAndUser(any(), any())).thenReturn(mockUser);

        assertThrows(NotFoundException.class, ()-> service.deletePost(mockPost.getId(), mockPost.getCommunity().getId(), userDTO));
        verify(repository, times(0)).deletePostById(any());
    }

    @Test
    public void deletePostShouldThrowForbiddenExceptionWhenRequestingUserIsNotInsideCommunity(){
        var mockUser = MockUsers.getUser();
        var mockPost = MockPosts.getPost();
        var userDTO = MockUsers.getUserTokenDTO();

        when(communityRepository.existsById(any())).thenReturn(true);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(null);

        when(repository.existsById(any())).thenReturn(true);
        when(repository.findById(any())).thenReturn(Optional.of(mockPost));

        when(communityRepository.getModeratorByCommunityAndUser(any(), any())).thenReturn(mockUser);

        assertThrows(ForbiddenException.class, ()-> service.deletePost(mockPost.getId(), mockPost.getCommunity().getId(), userDTO));
        verify(repository, times(0)).deletePostById(any());
    }

    @Test
    public void deletePostShouldThrowNotFoundExceptionWhenThePostWasNotFound(){
        var mockUser = MockUsers.getUser();
        var mockPost = MockPosts.getPost();
        var userDTO = MockUsers.getUserTokenDTO();

        when(communityRepository.existsById(any())).thenReturn(true);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(mockUser);

        when(repository.existsById(any())).thenReturn(false);
        when(repository.findById(any())).thenReturn(Optional.of(mockPost));

        when(communityRepository.getModeratorByCommunityAndUser(any(), any())).thenReturn(mockUser);

        assertThrows(NotFoundException.class, ()-> service.deletePost(mockPost.getId(), mockPost.getCommunity().getId(), userDTO));
        verify(repository, times(0)).deletePostById(any());
    }

    @Test
    public void deletePostShouldThrowForbiddenExceptionWhenUserDoesNotHaveAnyPrivilegesNeitherIsTheCreator(){
        var mockUser = MockUsers.getUser();
        var mockPost = MockPosts.getPost();
        var userDTO = MockUsers.getUserTokenDTO();
        userDTO.setId(2L);

        when(communityRepository.existsById(any())).thenReturn(true);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(mockUser);

        when(repository.existsById(any())).thenReturn(true);
        when(repository.findById(any())).thenReturn(Optional.of(mockPost));

        when(communityRepository.getModeratorByCommunityAndUser(any(), any())).thenReturn(null);

        assertThrows(ForbiddenException.class, ()-> service.deletePost(mockPost.getId(), mockPost.getCommunity().getId(), userDTO));
        verify(repository, times(0)).deletePostById(any());
    }

    // getAllPostsFromCommunityFromUser
    @Test
    public void getAllPostsFromCommunityFromUserShouldGetAllPostsFromRegisteredUser(){
        var mockUser = MockUsers.getUser();
        var postList = Stream.of(MockPosts.getPost()).toList();
        var userDTO = MockUsers.getUserTokenDTO();

        when(communityRepository.existsById(any())).thenReturn(true);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(mockUser);
        when(repository.getAllPostsFromUserByCommunityById(any(), any())).thenReturn(postList);

        assertDoesNotThrow(()->{
            var result = service.getAllPostsFromCommunityFromUser(1L, userDTO);
            assertNotNull(result);
        });
    }
    @Test
    public void getAllPostsFromCommunityFromUserShouldThrowNotFoundExceptionWhenCommunityIsNotFound(){
        var mockUser = MockUsers.getUser();
        var postList = Stream.of(MockPosts.getPost()).toList();
        var userDTO = MockUsers.getUserTokenDTO();

        when(communityRepository.existsById(any())).thenReturn(false);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(mockUser);
        when(repository.getAllPostsFromUserByCommunityById(any(), any())).thenReturn(postList);

        assertThrows(NotFoundException.class, ()-> service.getAllPostsFromCommunityFromUser(1L, userDTO));
    }
    @Test
    public void getAllPostsFromCommunityFromUserShouldThrowForbiddenExceptionWhenUserDoesNotBelongToCommunity(){
        var postList = Stream.of(MockPosts.getPost()).toList();
        var userDTO = MockUsers.getUserTokenDTO();

        when(communityRepository.existsById(any())).thenReturn(true);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(null);
        when(repository.getAllPostsFromUserByCommunityById(any(), any())).thenReturn(postList);

        assertThrows(ForbiddenException.class, ()-> service.getAllPostsFromCommunityFromUser(1L, userDTO));
    }

    // getAllPostsFromCommunity
    @Test
    public void getAllPostsFromCommunityShouldGetAllPostsFromRegisteredUser(){
        var mockUser = MockUsers.getUser();
        var postList = Stream.of(MockPosts.getPost()).toList();
        var userDTO = MockUsers.getUserTokenDTO();

        when(communityRepository.existsById(any())).thenReturn(true);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(mockUser);
        when(repository.getAllPostsFromCommunityById(any())).thenReturn(postList);

        assertDoesNotThrow(()->{
            var result = service.getAllPostsFromCommunity(1L, userDTO);
            assertNotNull(result);
        });
    }

    @Test
    public void getAllPostsFromCommunityShouldThrowNotFoundExceptionWhenCommunityIsNotFound(){
        var mockUser = MockUsers.getUser();
        var postList = Stream.of(MockPosts.getPost()).toList();
        var userDTO = MockUsers.getUserTokenDTO();

        when(communityRepository.existsById(any())).thenReturn(false);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(mockUser);
        when(repository.getAllPostsFromUserByCommunityById(any(), any())).thenReturn(postList);

        assertThrows(NotFoundException.class, ()-> service.getAllPostsFromCommunity(1L, userDTO));
    }
    @Test
    public void getAllPostsFromCommunityShouldThrowForbiddenExceptionWhenUserDoesNotBelongToCommunity(){
        var postList = Stream.of(MockPosts.getPost()).toList();
        var userDTO = MockUsers.getUserTokenDTO();

        when(communityRepository.existsById(any())).thenReturn(true);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(null);
        when(repository.getAllPostsFromUserByCommunityById(any(), any())).thenReturn(postList);

        assertThrows(ForbiddenException.class, ()-> service.getAllPostsFromCommunity(1L, userDTO));
    }

    // editPost
    @Test
    public void editPostShouldEditExistingPostOfPostCreatorWithBothProductAndServiceRegistered() {
        var mockProduct = MockProducts.getProduct();
        var mockService = MockServices.getService();
        var mockPost = MockPosts.getPost();
        var mockuser = MockUsers.getUser();
        var mockUserDTO = MockUsers.getUserTokenDTO();

        var request = MockRequests.getEditPostRequest();

        when(communityRepository.existsById(any())).thenReturn(true);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(mockuser);
        when(repository.existsById(any())).thenReturn(true);
        when(repository.findById(any())).thenReturn(Optional.of(mockPost));
        when(communityRepository.getModeratorByCommunityAndUser(any(), any())).thenReturn(null);

        when(productRepository.existsById(any())).thenReturn(true);
        when(productRepository.save(any())).thenReturn(mockProduct);
        when(productRepository.findById(any())).thenReturn(Optional.of(mockProduct));

        when(serviceRepository.existsById(any())).thenReturn(true);
        when(serviceRepository.save(any())).thenReturn(mockService);
        when(serviceRepository.findById(any())).thenReturn(Optional.of(mockService));

        assertDoesNotThrow(()->{
            var result = service.editPost(request, 1L, 1L, mockUserDTO);
            assertNotNull(result);
            verify(repository, times(1)).save(any());
        });
    }

    @Test
    public void editPostShouldEditExistingPostOfPostCreatorEvenWithBothProductAndServiceNotYetRegistered() {
        var mockProduct = MockProducts.getProduct();
        var mockService = MockServices.getService();
        var mockPost = MockPosts.getPost();
        var mockuser = MockUsers.getUser();
        var mockUserDTO = MockUsers.getUserTokenDTO();

        var request = MockRequests.getEditPostRequest();

        when(communityRepository.existsById(any())).thenReturn(true);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(mockuser);
        when(repository.existsById(any())).thenReturn(true);
        when(repository.findById(any())).thenReturn(Optional.of(mockPost));
        when(communityRepository.getModeratorByCommunityAndUser(any(), any())).thenReturn(null);

        when(productRepository.existsById(any())).thenReturn(false);
        when(productRepository.save(any())).thenReturn(mockProduct);
        when(productRepository.findById(any())).thenReturn(Optional.of(mockProduct));

        when(serviceRepository.existsById(any())).thenReturn(false);
        when(serviceRepository.save(any())).thenReturn(mockService);
        when(serviceRepository.findById(any())).thenReturn(Optional.of(mockService));

        assertDoesNotThrow(()->{
            var result = service.editPost(request, 1L, 1L, mockUserDTO);
            assertNotNull(result);
            verify(repository, times(1)).save(any());
        });
    }

    @Test
    public void editPostShouldThrowNotFoundExceptionWhenTheCommunityDoesNotExist() {
        var mockProduct = MockProducts.getProduct();
        var mockService = MockServices.getService();
        var mockPost = MockPosts.getPost();
        var mockuser = MockUsers.getUser();
        var mockUserDTO = MockUsers.getUserTokenDTO();

        var request = MockRequests.getEditPostRequest();

        when(communityRepository.existsById(any())).thenReturn(false);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(mockuser);
        when(repository.existsById(any())).thenReturn(true);
        when(repository.findById(any())).thenReturn(Optional.of(mockPost));
        when(communityRepository.getModeratorByCommunityAndUser(any(), any())).thenReturn(null);

        when(productRepository.existsById(any())).thenReturn(false);
        when(productRepository.save(any())).thenReturn(mockProduct);
        when(productRepository.findById(any())).thenReturn(Optional.of(mockProduct));

        when(serviceRepository.existsById(any())).thenReturn(false);
        when(serviceRepository.save(any())).thenReturn(mockService);
        when(serviceRepository.findById(any())).thenReturn(Optional.of(mockService));

        assertThrows(NotFoundException.class, ()->
                service.editPost(request, 1L, 1L, mockUserDTO));
    }

    @Test
    public void editPostShouldThrowForbiddenExceptionWhenUserIsNotInsideCommunity() {
        var mockProduct = MockProducts.getProduct();
        var mockService = MockServices.getService();
        var mockPost = MockPosts.getPost();
        var mockUserDTO = MockUsers.getUserTokenDTO();

        var request = MockRequests.getEditPostRequest();

        when(communityRepository.existsById(any())).thenReturn(true);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(null);
        when(repository.existsById(any())).thenReturn(true);
        when(repository.findById(any())).thenReturn(Optional.of(mockPost));
        when(communityRepository.getModeratorByCommunityAndUser(any(), any())).thenReturn(null);

        when(productRepository.existsById(any())).thenReturn(false);
        when(productRepository.save(any())).thenReturn(mockProduct);
        when(productRepository.findById(any())).thenReturn(Optional.of(mockProduct));

        when(serviceRepository.existsById(any())).thenReturn(false);
        when(serviceRepository.save(any())).thenReturn(mockService);
        when(serviceRepository.findById(any())).thenReturn(Optional.of(mockService));


        assertThrows(ForbiddenException.class, ()->
                service.editPost(request, 1L, 1L, mockUserDTO));
    }

    @Test
    public void editPostShouldThrowNotFoundExceptionWhenPostIsNotFound() {
        var mockProduct = MockProducts.getProduct();
        var mockService = MockServices.getService();
        var mockPost = MockPosts.getPost();
        var mockuser = MockUsers.getUser();
        var mockUserDTO = MockUsers.getUserTokenDTO();

        var request = MockRequests.getEditPostRequest();

        when(communityRepository.existsById(any())).thenReturn(true);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(mockuser);
        when(repository.existsById(any())).thenReturn(false);
        when(repository.findById(any())).thenReturn(Optional.of(mockPost));
        when(communityRepository.getModeratorByCommunityAndUser(any(), any())).thenReturn(null);

        when(productRepository.existsById(any())).thenReturn(false);
        when(productRepository.save(any())).thenReturn(mockProduct);
        when(productRepository.findById(any())).thenReturn(Optional.of(mockProduct));

        when(serviceRepository.existsById(any())).thenReturn(false);
        when(serviceRepository.save(any())).thenReturn(mockService);
        when(serviceRepository.findById(any())).thenReturn(Optional.of(mockService));

        assertThrows(NotFoundException.class, ()->
                service.editPost(request, 1L, 1L, mockUserDTO));
    }

    @Test
    public void editPostShouldThrowForbiddenExceptionWhenUserDoesNotHavePermissionToUpdatePost() {
        var mockProduct = MockProducts.getProduct();
        var mockService = MockServices.getService();
        var mockPost = MockPosts.getPost();
        var mockuser = MockUsers.getUser();
        var anotherMockUser = MockUsers.getUser();
        var mockUserDTO = MockUsers.getUserTokenDTO();

        anotherMockUser.setId(2L);
        mockPost.setCreator(anotherMockUser);
        var request = MockRequests.getEditPostRequest();

        when(communityRepository.existsById(any())).thenReturn(true);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(mockuser);
        when(repository.existsById(any())).thenReturn(true);
        when(repository.findById(any())).thenReturn(Optional.of(mockPost));
        when(communityRepository.getModeratorByCommunityAndUser(any(), any())).thenReturn(null);

        when(productRepository.existsById(any())).thenReturn(false);
        when(productRepository.save(any())).thenReturn(mockProduct);
        when(productRepository.findById(any())).thenReturn(Optional.of(mockProduct));

        when(serviceRepository.existsById(any())).thenReturn(false);
        when(serviceRepository.save(any())).thenReturn(mockService);
        when(serviceRepository.findById(any())).thenReturn(Optional.of(mockService));

        assertThrows(ForbiddenException.class, ()->
                service.editPost(request, 1L, 1L, mockUserDTO));
    }

    @Test
    public void editPostShouldAllowAModeratorToEditAPostThatIsNotHisOwn() {
        var mockProduct = MockProducts.getProduct();
        var mockService = MockServices.getService();
        var mockPost = MockPosts.getPost();
        var mockuser = MockUsers.getUser();
        var anotherMockUser = MockUsers.getUser();
        var mockUserDTO = MockUsers.getUserTokenDTO();

        anotherMockUser.setId(2L);
        mockPost.setCreator(anotherMockUser);
        var request = MockRequests.getEditPostRequest();

        when(communityRepository.existsById(any())).thenReturn(true);
        when(communityRepository.getUserInCommunityById(any(), any())).thenReturn(mockuser);
        when(repository.existsById(any())).thenReturn(true);
        when(repository.findById(any())).thenReturn(Optional.of(mockPost));
        when(communityRepository.getModeratorByCommunityAndUser(any(), any())).thenReturn(mockuser);

        when(productRepository.existsById(any())).thenReturn(false);
        when(productRepository.save(any())).thenReturn(mockProduct);
        when(productRepository.findById(any())).thenReturn(Optional.of(mockProduct));

        when(serviceRepository.existsById(any())).thenReturn(false);
        when(serviceRepository.save(any())).thenReturn(mockService);
        when(serviceRepository.findById(any())).thenReturn(Optional.of(mockService));

        assertDoesNotThrow(()->{
            var result = service.editPost(request, 1L, 1L, mockUserDTO);
            assertNotNull(result);
            verify(repository, times(1)).save(any());
        });
    }
}
