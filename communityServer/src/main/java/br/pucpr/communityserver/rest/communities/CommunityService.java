package br.pucpr.communityserver.rest.communities;

import br.pucpr.communityserver.lib.exception.ForbiddenException;
import br.pucpr.communityserver.lib.exception.NotFoundException;
import br.pucpr.communityserver.rest.communities.requests.CommunityJoinRequest;
import br.pucpr.communityserver.rest.communities.requests.CommunityRequest;
import br.pucpr.communityserver.rest.communities.responses.CommunityResponse;
import br.pucpr.communityserver.rest.users.User;
import br.pucpr.communityserver.rest.users.UserRepository;
import br.pucpr.communityserver.rest.users.requests.UserTokenDTO;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CommunityService {

    private final CommunityRepository repository;
    private final UserRepository userRepository;

    public CommunityService(CommunityRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public CommunityResponse saveCommunity(CommunityRequest communityRequest){
        Community community = new Community(communityRequest);

        String code = "";
        Community aux = new Community();
        while(aux != null){
            int CODE_LENGTH = 8;
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

    public List<CommunityResponse> listAllCommunitiesFromUser(Long userId){
        var communities = repository.getAllCommunitiesByUserId(userId);
        return communities.stream().map(CommunityResponse::new).toList();
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

    public void joinCommunity(UserTokenDTO tokenDTO, CommunityJoinRequest request){
        var name = request.getName();
        var code = request.getCode();
        var community = repository.getCommunitiesByCodeAndName(name, code);
        if(community == null) throw new NotFoundException("Incorrect community or code");

        var user = new User(tokenDTO);
        if(!userRepository.existsById(user.getId())) userRepository.save(user);
        if(repository.getUserInCommunityById(community.getId(), user.getId()) != null) throw new ForbiddenException("User is already inside this community");

        user = userRepository.findById(user.getId()).get();

        community.getUsers().add(user);
        repository.save(community);
    }


    public void leaveCommunity(UserTokenDTO tokenDTO, Long communityId) {
        if(!repository.existsById(communityId)) throw new NotFoundException("Community not found");

        var community = repository.getCommunityById(communityId);
        if(repository.getUserInCommunityById(communityId, tokenDTO.getId()) == null) throw new NotFoundException("User not found inside community");
        var user = userRepository.findById(tokenDTO.getId()).get();

        community.getUsers().removeIf(u -> u.getId().intValue() == user.getId().intValue());
        repository.save(community);

//        var userList = repository.getUsersFromCommunityById(communityId);
//        community.setUsers(userList.removeIf(u -> u.getId().intValue() == user.getId().intValue());
//        repository.updateCommunityById(communityId, community);
    }


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
