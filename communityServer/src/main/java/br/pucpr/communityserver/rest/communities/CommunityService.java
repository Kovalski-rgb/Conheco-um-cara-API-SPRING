package br.pucpr.communityserver.rest.communities;

import br.pucpr.communityserver.lib.exception.ForbiddenException;
import br.pucpr.communityserver.lib.exception.NotFoundException;
import br.pucpr.communityserver.rest.communities.requests.CommunityJoinRequest;
import br.pucpr.communityserver.rest.communities.requests.CommunityRequest;
import br.pucpr.communityserver.rest.communities.requests.RequestToggleModerator;
import br.pucpr.communityserver.rest.communities.responses.CommunityResponse;
import br.pucpr.communityserver.rest.communities.responses.GetModeratorResponse;
import br.pucpr.communityserver.rest.users.User;
import br.pucpr.communityserver.rest.users.UserRepository;
import br.pucpr.communityserver.rest.users.requests.UserTokenDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommunityService {

    private final CommunityRepository repository;
    private final UserRepository userRepository;

    public CommunityService(CommunityRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public CommunityResponse saveCommunity(UserTokenDTO userTokenDTO, CommunityRequest communityRequest){
        Community community = new Community(communityRequest);

        String code = "";
        Community aux = new Community();
        while(aux != null){
            int CODE_LENGTH = 8;
            code = generateCode(CODE_LENGTH);
            aux = repository.getCommunitiesByCodeAndName(community.getName(), code);
        }
        var user = new User(userTokenDTO);
        if(!userRepository.existsById(user.getId())) userRepository.save(user);
        var moderators = new HashSet<User>();
        moderators.add(user);
        community.setUsers(new HashSet<>());
        community.setCreatedAt(LocalDateTime.now());
        community.setCode(code);
        community.setModerators(moderators);
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

    public CommunityResponse editCommunity(UserTokenDTO user, Long targetCommunityId, CommunityRequest request){
        if(!repository.existsById(targetCommunityId)) { throw new NotFoundException("Target community not found"); };
        if(repository.getModeratorByCommunityAndUser(targetCommunityId, user.getId()) == null &&
                !user.getRoles().contains("ADMIN"))
            throw new ForbiddenException("User does not have permission to edit this community");

        Community communityToBeUpdated = repository.findById(targetCommunityId).get();
        Community community = new Community(request);
        communityToBeUpdated.setId(targetCommunityId);
        communityToBeUpdated.setName(community.getName());
        communityToBeUpdated.setDescription(community.getDescription());

        return new CommunityResponse(repository.save(communityToBeUpdated));
    }

    public void deleteCommunity(UserTokenDTO user, Long communityId){
        if(!repository.existsById(communityId)) { throw new NotFoundException("Community not found"); };
        if(repository.getModeratorByCommunityAndUser(communityId, user.getId()) != null ||
                user.getRoles().contains("ADMIN")){
            repository.deleteCommunityById(communityId);
            return;
        }
        throw new ForbiddenException("User does not have permission to delete this community");
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

        community.getModerators().removeIf(m -> m.getId().intValue() == user.getId().intValue());
        community.getUsers().removeIf(u -> u.getId().intValue() == user.getId().intValue());

        if(repository.getModeratorCountFromCommunityById(communityId).size() == 0){
            if(community.getUsers().size() > 0) {
                for(User u : community.getUsers()){
                    if(!u.getId().equals(tokenDTO.getId())) {
                        community.getModerators().add(u);
                        break;
                    }
                }
            }
        }

        repository.save(community);

        if(repository.getUserCountFromCommunityById(communityId).size() == 0){
            repository.deleteCommunityById(communityId);
        }
//        var userList = repository.getUsersFromCommunityById(communityId);
//        community.setUsers(userList.removeIf(u -> u.getId().intValue() == user.getId().intValue());
//        repository.updateCommunityById(communityId, community);
    }

    public List<GetModeratorResponse> listAllModeratorsFromCommunity(Long userId, Long communityId){
        if(!repository.existsById(communityId))
            throw new NotFoundException("Community not found");
        if(repository.getUserInCommunityById(communityId, userId) == null)
            throw new ForbiddenException("User does not belong to specified community");
        return repository.getAllModeratorsByCommunityId(communityId)
                .stream()
                .map( u ->
                        new GetModeratorResponse(u.getId())
                ).collect(Collectors.toList());
    }

    public void toggleModerator(UserTokenDTO userDTO, RequestToggleModerator request){
        if(!repository.existsById(request.getCommunityId()))
            throw new NotFoundException("Community not found");
        if(repository.getUserInCommunityById(request.getCommunityId(), userDTO.getId()) == null)
            throw new ForbiddenException("User does not belong to specified community");
        if(repository.getUserInCommunityById(request.getCommunityId(), request.getUserId()) == null)
            throw new ForbiddenException("Target user does not belong to specified community");
        if(repository.getModeratorByCommunityAndUser(request.getCommunityId(), userDTO.getId()) == null)
            throw new ForbiddenException("User does not have privileges to toggle moderator status");
        var community = repository.getCommunityById(request.getCommunityId());

        if(community.getUsers().size() == 1 && community.getModerators().size() == 1)
            throw new ForbiddenException("A community cannot have no moderators");

        var before = community.getModerators();

        community.setModerators(
            community.getModerators()
                    .stream()
                    .filter(m -> !m.getId().equals(request.getUserId()))
                    .collect(Collectors.toSet())
        );

        if(before.size() == community.getModerators().size()){
            var user = userRepository.findById(request.getUserId()).get();
            community.getModerators().add(user);
        }

        System.out.println(community.getModerators());
        System.out.println(community.getUsers());

        if(community.getModerators().size() == 0){
            for(User u : community.getUsers()){
                if(!u.getId().equals(request.getUserId())) {
                    community.getModerators().add(u);
                    break;
                }
            }
        }

        repository.save(community);
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
