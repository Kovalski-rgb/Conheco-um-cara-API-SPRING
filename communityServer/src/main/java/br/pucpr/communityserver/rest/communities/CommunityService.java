package br.pucpr.communityserver.rest.communities;

import br.pucpr.communityserver.lib.exception.ForbiddenException;
import br.pucpr.communityserver.lib.exception.NotFoundException;
import br.pucpr.communityserver.rest.communities.requests.CommunityJoinRequest;
import br.pucpr.communityserver.rest.communities.requests.CommunityRequest;
import br.pucpr.communityserver.rest.communities.requests.RequestToggleModerator;
import br.pucpr.communityserver.rest.communities.responses.CommunityResponse;
import br.pucpr.communityserver.rest.communities.responses.GetModeratorResponse;
import br.pucpr.communityserver.rest.communities.responses.MultipleCommunitiesResponse;
import br.pucpr.communityserver.rest.users.User;
import br.pucpr.communityserver.rest.users.UserRepository;
import br.pucpr.communityserver.rest.users.requests.UserTokenDTO;
import br.pucpr.communityserver.rest.users.responses.UserResponse;
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

        var allCodes = repository.getCodesFromAllCommunitiesByName(communityRequest.getName());
        if(allCodes == null) throw new IllegalStateException("could not check for a valid community code");

        String code = generateCode(Community.CODE_LENGTH);
        long count = 0;

        while(allCodes.contains(code)){
            if(count == 2_821_109_907_456L){
                // not that this'll be triggered any time soon, you'd need around 2,147,483,647 communities with the same name
                // to "break" a set
                throw new ForbiddenException("Community name unavailable, please create your community with another name");
            }
            code = generateCode(Community.CODE_LENGTH);
            count++;
        }
        var user = new User(userTokenDTO);
        if(!userRepository.existsById(user.getId())) userRepository.save(user);
        var moderators = new HashSet<User>();
        moderators.add(user);
        community.setUsers(new HashSet<>());
        community.setCreatedAt(LocalDateTime.now());
        community.setCode(code);
        community.setModerators(moderators);
        community.setPosts(new ArrayList<>());
        return new CommunityResponse(repository.save(community));
    }

    // TODO try to add pagination later
    public List<MultipleCommunitiesResponse> listAllCommunities(){
        List<Community> communities = repository.findAll();
        return communities.stream().map(MultipleCommunitiesResponse::new).collect(Collectors.toList());
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
        if(repository.getCommunitiesByCodeAndName(request.getName(), communityToBeUpdated.getCode()) != null){
            throw new ForbiddenException("Community with another name-code combination found, please chose another name");
        }
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

    public void kickFromCommunity(UserTokenDTO userDTO, Long userId, Long communityId){
        if(!repository.existsById(communityId))
            throw new NotFoundException("Community not found");
        if(repository.getUserInCommunityById(communityId, userId) == null)
            throw new ForbiddenException("Target user does not belong to specified community");

        if(repository.getModeratorByCommunityAndUser(communityId, userDTO.getId()) != null ||
                userDTO.getRoles().contains("ADMIN"))
        {
            var aux = new UserTokenDTO();
            aux.setId(userId);
            leaveCommunity(aux, communityId);
            return;
        }
        throw new ForbiddenException("User does not have privileges to kick another user");
    }

    public void leaveCommunity(UserTokenDTO tokenDTO, Long communityId) {
        if(!repository.existsById(communityId)) throw new NotFoundException("Community not found");
        if(repository.getUserInCommunityById(communityId, tokenDTO.getId()) == null) throw new NotFoundException("User not found inside community");

        var community = repository.getCommunityById(communityId);
        var user = userRepository.findById(tokenDTO.getId()).get();

        community.getModerators().removeIf(m -> m.getId().equals(user.getId()));
        community.getUsers().removeIf(u -> u.getId().equals(user.getId()));

        if(repository.getModeratorListFromCommunityById(communityId).size() == 0){
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

        if(repository.getUserListFromCommunityById(communityId).size() == 0){
            repository.deleteCommunityById(communityId);
        }
//        var userList = repository.getUsersFromCommunityById(communityId);
//        community.setUsers(userList.removeIf(u -> u.getId().intValue() == user.getId().intValue());
//        repository.updateCommunityById(communityId, community);
    }

    public CommunityResponse getCommunityById(Long id){
        return repository.existsById(id) ? new CommunityResponse(repository.findById(id).get()) : null;
    }

    public List<GetModeratorResponse> listAllModeratorsFromCommunity(Long userId, Long communityId){
        if(!repository.existsById(communityId))
            throw new NotFoundException("Community not found");
        if(repository.getUserInCommunityById(communityId, userId) == null)
            throw new ForbiddenException("User does not belong to specified community");
        return repository.getModeratorListFromCommunityById(communityId)
                .stream()
                .map( u ->
                        new GetModeratorResponse(u.getId())
                ).collect(Collectors.toList());
    }


    public List<UserResponse> listMembersFromCommunity(Long userId, Long communityId){
        if(!repository.existsById(communityId))
            throw new NotFoundException("Community not found");
        if(repository.getUserInCommunityById(communityId, userId) == null)
            throw new ForbiddenException("User does not belong to specified community");
        return repository.getUserListFromCommunityById(communityId)
                .stream().map(UserResponse::new).collect(Collectors.toList());
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

    public String generateCode(int size){
        String baseChars = "abcefghijklmnopqrstuvwxzy0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < size; i++){
            code.append(baseChars.charAt(random.nextInt(baseChars.length())));
        }
        return code.toString();
    }
}
