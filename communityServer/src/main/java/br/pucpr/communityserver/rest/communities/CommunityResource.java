package br.pucpr.communityserver.rest.communities;

import br.pucpr.communityserver.lib.security.JWT;
import br.pucpr.communityserver.rest.communities.requests.CommunityJoinRequest;
import br.pucpr.communityserver.rest.communities.requests.CommunityRequest;
import br.pucpr.communityserver.rest.communities.requests.RequestToggleModerator;
import br.pucpr.communityserver.rest.communities.responses.CommunityResponse;
import br.pucpr.communityserver.rest.communities.responses.GetModeratorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "Community endpoints")
public class CommunityResource {

    private final CommunityService service;
    private final JWT jwt;

    public CommunityResource(CommunityService service, JWT jwt) {
        this.service = service;
        this.jwt = jwt;
    }

    @PostMapping("/create")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER"})
    public ResponseEntity<CommunityResponse> createCommunity(
        HttpServletRequest headers,
        @Valid @RequestBody CommunityRequest request
    ){
        String token = headers.getHeader("Authorization");
        var user = jwt.decode(token);

        var response = service.saveCommunity(user, request);
        if(response == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        service.joinCommunity(user, new CommunityJoinRequest(response.getName(), response.getCode()));
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
    @RolesAllowed({"USER", "ADMIN"})
    public ResponseEntity<CommunityResponse> editCommunity(
            HttpServletRequest headers,
            @RequestParam Long id,
            @Valid @RequestBody CommunityRequest request
    ){
        String token = headers.getHeader("Authorization");
        var response = service.editCommunity(jwt.decode(token), id, request);
        if(response==null){ ResponseEntity.status(HttpStatus.NOT_FOUND).build(); }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER", "ADMIN"})
    public void deleteCommunity(
            HttpServletRequest headers,
            @RequestParam Long id
    ){
        String token = headers.getHeader("Authorization");
        service.deleteCommunity(jwt.decode(token), id);
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

    //TODO create /kick route for admin and mod use only

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

    // moderators
    @GetMapping("moderators/{id}")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER"})
    public ResponseEntity<List<GetModeratorResponse>> listModeratorsFromCommunity(
        HttpServletRequest headers,
        @RequestParam @Valid Long communityId
    ){
        String token = headers.getHeader("Authorization");
        var result = service.listAllModeratorsFromCommunity(jwt.decode(token).getId(), communityId);
        if(result == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(result);

    }


    @PostMapping("moderators/toggle")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER"})
    public void toggleModeratorUser(
        HttpServletRequest headers,
        @RequestBody @Valid RequestToggleModerator request
    ){
        String token = headers.getHeader("Authorization");
        service.toggleModerator(jwt.decode(token), request);
    }

}
