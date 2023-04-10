package br.pucpr.communityserver.rest.posts;

import br.pucpr.communityserver.lib.security.JWT;
import br.pucpr.communityserver.rest.posts.requests.EditPostRequest;
import br.pucpr.communityserver.rest.posts.requests.PostRequest;
import br.pucpr.communityserver.rest.posts.response.PostResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostResource {

    private final PostService service;
    private final JWT jwt;
    public PostResource(PostService service, JWT jwt) {
        this.service = service;
        this.jwt = jwt;
    }

    @PostMapping("/create")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER"})
    public ResponseEntity<PostResponse> createPost(
        HttpServletRequest headers,
        @Valid @RequestBody PostRequest request
    ){
        String token = headers.getHeader("Authorization");
        var response = service.savePost(jwt.decode(token), request);
        if(response == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(new PostResponse(response));
    }

    @GetMapping("/me")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER"})
    public ResponseEntity<List<PostResponse>> getAllPostsFromUser(
            HttpServletRequest headers
    ) {
        String token = headers.getHeader("Authorization");
        var response = service.getAllPostsFromUser(jwt.decode(token));
        if(response == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/community/{id}")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER"})
    public ResponseEntity<List<PostResponse>> getAllPostsFromCommunity(
            @Valid @PathVariable Long id,
            HttpServletRequest headers
    ) {
        String token = headers.getHeader("Authorization");
        var response = service.getAllPostsFromCommunity(id, jwt.decode(token));
        if(response == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/community/{id}/me")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER"})
    public ResponseEntity<List<PostResponse>> getAllPostsFromCommunityFromUser(
            @Valid @PathVariable Long id,
            HttpServletRequest headers
    ) {
        String token = headers.getHeader("Authorization");
        var response = service.getAllPostsFromCommunityFromUser(id, jwt.decode(token));
        if(response == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}/community/{communityId}")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER"})
    public ResponseEntity<HttpStatus> deletePost(
            @Valid @PathVariable Long postId,
            @Valid @PathVariable Long communityId,
            HttpServletRequest headers
    ) {
        String token = headers.getHeader("Authorization");
        service.deletePost(postId, communityId, jwt.decode(token));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{postId}/community/{communityId}")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER"})
    public ResponseEntity<PostResponse> deletePost(
            @Valid @PathVariable Long postId,
            @Valid @PathVariable Long communityId,
            @Valid @RequestBody EditPostRequest request,
            HttpServletRequest headers
    ) {
        String token = headers.getHeader("Authorization");
        var response = service.editPost(request, postId, communityId, jwt.decode(token));
        if(response == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(response);
    }


}
