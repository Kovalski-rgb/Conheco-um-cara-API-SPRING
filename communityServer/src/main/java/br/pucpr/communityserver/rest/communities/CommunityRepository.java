package br.pucpr.communityserver.rest.communities;

import br.pucpr.communityserver.rest.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    public Community getCommunitiesByCodeAndName(String name, String code);

    public User getUserInCommunityById(Long communityId, Long userId);

    public Community getCommunityById(Long id);

    public Set<User> getUsersFromCommunityById(Long id);

    public List<Community> getAllCommunitiesByUserId(Long userId);

//    @Modifying
//    public void updateCommunityById(Long id, Community community);

}
