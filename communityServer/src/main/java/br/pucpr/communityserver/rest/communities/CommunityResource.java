package br.pucpr.communityserver.rest.communities;

import br.pucpr.communityserver.lib.security.JWT;
import br.pucpr.communityserver.rest.communities.requests.CommunityJoinRequest;
import br.pucpr.communityserver.rest.communities.requests.CommunityRequest;
import br.pucpr.communityserver.rest.communities.responses.CommunityResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.Transient;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community")
public class CommunityResource {

    private CommunityService service;
    private JWT jwt;

    public CommunityResource(CommunityService service, JWT jwt) {
        this.service = service;
        this.jwt = jwt;
    }

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<CommunityResponse> createCommunity(
        @Valid @RequestBody CommunityRequest request
    ){
        var response = service.saveCommunity(request);
        if(response == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/join")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER"})
    public void joinCommunity(
            HttpServletRequest headers,
            @Valid @RequestBody CommunityJoinRequest request
    ){
        String token = headers.getHeader("Authorization");
        service.joinCommunity(jwt.decode(token), request);
    }

    @GetMapping("/all")
    @Transactional
    public List<CommunityResponse> listCommunities(){
        return service.listAllCommunities();
    }

    @PutMapping("{id}")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<CommunityResponse> editCommunity(
            @RequestParam Long id,
            @Valid @RequestBody CommunityRequest request
    ){
        var response = service.editCommunity(id, request);
        if(response==null){ ResponseEntity.status(HttpStatus.NOT_FOUND).build(); }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"ADMIN"})
    public void deleteCommunity(
            @RequestParam Long id
    ){
        service.deleteCommunity(id);
    }

    @PostMapping("/leave")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER"})
    public void leaveCommunity(
            HttpServletRequest headers,
            @RequestParam Long communityId
    ){
        String token = headers.getHeader("Authorization");
        service.leaveCommunity(jwt.decode(token), communityId);
    }

    @GetMapping("/me")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER"})
    public List<CommunityResponse> listCommunities(
            HttpServletRequest headers
    ){
        String token = headers.getHeader("Authorization");
        return service.listAllCommunitiesFromUser(jwt.decode(token).getId());
    }

}
