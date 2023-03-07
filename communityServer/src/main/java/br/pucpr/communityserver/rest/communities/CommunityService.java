package br.pucpr.communityserver.rest.communities;

import br.pucpr.communityserver.lib.exception.NotFoundException;
import br.pucpr.communityserver.rest.communities.requests.CommunityRequest;
import br.pucpr.communityserver.rest.communities.responses.CommunityResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CommunityService {

    private final int CODE_LENGTH = 8;
    private CommunityRepository repository;

    public CommunityService(CommunityRepository communityRepository) {
        this.repository = communityRepository;
    }

    public CommunityResponse saveCommunity(CommunityRequest communityRequest){
        Community community = new Community(communityRequest);

        String code = "";
        Community aux = new Community();
        while(aux != null){
            code = generateCode(CODE_LENGTH);
            aux = repository.getCommunitiesByCodeAndName(community.getName(), code);
        }
        community.setCreatedAt(LocalDateTime.now());
        community.setCode(code);
        return new CommunityResponse(repository.save(community));
    }

    public List<CommunityResponse> listAllCommunities(){
        List<Community> communities = repository.findAll();
        List<CommunityResponse> response = new ArrayList<>();
        for(Community c : communities){
            response.add(new CommunityResponse(c));
        }
        return response;
    }

    public CommunityResponse editCommunity(Long targetCommunityId, CommunityRequest request){
        if(!repository.existsById(targetCommunityId)) { throw new NotFoundException("Target community not found"); };

        Community communityToBeUpdated = repository.findById(targetCommunityId).get();
        Community community = new Community(request);
        communityToBeUpdated.setId(targetCommunityId);
        communityToBeUpdated.setName(community.getName());
        communityToBeUpdated.setDescription(community.getDescription());

        return new CommunityResponse(repository.save(communityToBeUpdated));
    }

    public void deleteCommunity(Long communityId){
        if(!repository.existsById(communityId)) { throw new NotFoundException("Community not found"); };
        repository.deleteById(communityId);
    }

//    public

    private String generateCode(int size){
        String baseChars = "abcefghijklmnopqrstuvwxzy0123456789";
        var code = "";
        Random random = new Random();
        for(int i = 0; i < size; i++){
            code+= baseChars.charAt(random.nextInt(baseChars.length()));
        }
        return code;
    }
}
