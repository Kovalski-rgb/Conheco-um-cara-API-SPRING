package br.pucpr.communityserver.rest.communities;

import br.pucpr.communityserver.lib.security.JWT;
import br.pucpr.communityserver.rest.communities.requests.CommunityJoinRequest;
import br.pucpr.communityserver.rest.communities.requests.CommunityRequest;
import br.pucpr.communityserver.rest.communities.requests.RequestToggleModerator;
import br.pucpr.communityserver.rest.communities.responses.CommunityResponse;
import br.pucpr.communityserver.rest.communities.responses.GetModeratorResponse;
import br.pucpr.communityserver.rest.communities.responses.MultipleCommunitiesResponse;
import br.pucpr.communityserver.rest.users.User;
import br.pucpr.communityserver.rest.users.responses.UserResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "Creates a new community")
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


    @Operation(summary = "Requests user to join a community")
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


    @Operation(summary = "Lists every community, without their respective codes")
    @GetMapping("/all")
    @Transactional
    public ResponseEntity<List<MultipleCommunitiesResponse>> listCommunities(){
        return ResponseEntity.ok(service.listAllCommunities());
    }


    @Operation(summary = "Gets a specific community, with its code")
    @GetMapping("/{communityId}")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<CommunityResponse> getCommunityById(
            @Parameter(description = "The ID of the Community") @PathVariable @Valid Long communityId
    ){
        return ResponseEntity.ok(service.getCommunityById(communityId));
    }


    @Operation(summary = "Lists every member inside a specific community, only a participant may get this information")
    @GetMapping("{communityId}/members/")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER"})
    public ResponseEntity<List<UserResponse>> listMembersFromCommunity(
            HttpServletRequest headers,
            @Parameter(description = "The ID of the Community") @RequestParam @Valid Long communityId
    ){
        String token = headers.getHeader("Authorization");
        var result = service.listMembersFromCommunity(jwt.decode(token).getId(), communityId);
        if(result == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(result);

    }


    @Operation(summary = "Edits a community, only administrators and community moderators can edit a community")
    @PutMapping("{id}")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER", "ADMIN"})
    public ResponseEntity<CommunityResponse> editCommunity(
            HttpServletRequest headers,
            @Parameter(description = "The ID of the Community") @RequestParam Long id,
            @Valid @RequestBody CommunityRequest request
    ){
        String token = headers.getHeader("Authorization");
        var response = service.editCommunity(jwt.decode(token), id, request);
        if(response==null){ ResponseEntity.status(HttpStatus.NOT_FOUND).build(); }
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deletes a community, only administrators and community moderators can delete a community")
    @DeleteMapping("{id}")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER", "ADMIN"})
    public void deleteCommunity(
            HttpServletRequest headers,
            @Parameter(description = "The ID of the Community") @RequestParam Long id
    ){
        String token = headers.getHeader("Authorization");
        service.deleteCommunity(jwt.decode(token), id);
    }

    @Operation(summary = "Request to leave a community")
    @PostMapping("/leave")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER"})
    public void leaveCommunity(
            HttpServletRequest headers,
            @Parameter(description = "The ID of the Community") @RequestParam Long communityId
    ){
        String token = headers.getHeader("Authorization");
        service.leaveCommunity(jwt.decode(token), communityId);
    }


    @Operation(summary = "Request to kick a specific user from a community, only admins and community moderators may kick a user")
    @PostMapping("/{communityId}/kick/{userId}")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER", "ADMIN"})
    public void kickUserFromCommunity(
            HttpServletRequest headers,
            @Parameter(description = "The ID of the Community") @PathVariable Long communityId,
            @Parameter(description = "The ID of the User") @PathVariable Long userId
    ){
        String token = headers.getHeader("Authorization");
        service.kickFromCommunity(jwt.decode(token), userId, communityId);
    }


    @Operation(summary = "List all communities that the logged user belongs to")
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


    @Operation(summary = "List all moderators from a specific community")
    @GetMapping("{communityId}/moderators/")
    @Transactional
    @SecurityRequirement(name = "JWT-token")
    @RolesAllowed({"USER"})
    public ResponseEntity<List<GetModeratorResponse>> listModeratorsFromCommunity(
        HttpServletRequest headers,
        @Parameter(description = "The ID of the Community") @RequestParam @Valid Long communityId
    ){
        String token = headers.getHeader("Authorization");
        var result = service.listAllModeratorsFromCommunity(jwt.decode(token).getId(), communityId);
        if(result == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(result);

    }

    // TODO remove the existence of RequestToggleModerator, utilize @PathVariables like this /{communityId}/moderators/toggle/{userId}
    @Operation(summary = "Toggles moderator privileges on a specific member, only community moderators may toggle other user's privileges")
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
