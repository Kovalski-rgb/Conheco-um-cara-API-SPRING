package br.pucpr.communityserver.rest.communities;

import br.pucpr.communityserver.rest.communities.requests.CommunityJoinRequest;
import br.pucpr.communityserver.rest.communities.requests.CommunityRequest;
import br.pucpr.communityserver.rest.communities.responses.CommunityResponse;
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

    public CommunityResource(CommunityService communityService) {
        this.service = communityService;
    }

    @PostMapping
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
    public ResponseEntity<CommunityResponse> joinCommunity(
            // TODO find a way to get the user ID from the auth token
            ServletRequest headers,
            @Valid @RequestBody CommunityJoinRequest request
    ){
        System.out.println(headers == null? "it is null" : ((HttpServletRequest) headers).getAuthType());
        return null;
    }

    @GetMapping
    @Transactional
    public List<CommunityResponse> listCommunities(){
        return service.listAllCommunities();
    }

    @PutMapping("{id}")
    @Transactional
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
    public void deleteCommunity(
            @RequestParam Long id
    ){
        service.deleteCommunity(id);
    }

}
