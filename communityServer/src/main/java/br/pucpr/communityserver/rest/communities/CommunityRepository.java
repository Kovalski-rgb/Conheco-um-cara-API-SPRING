package br.pucpr.communityserver.rest.communities;

import br.pucpr.communityserver.rest.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    public Community getCommunitiesByCodeAndName(String name, String code);

    public User getUserInCommunityById(Long communityId, Long userId);

    public Community getCommunityById(Long id);

    public List<Community> getAllCommunitiesByUserId(Long userId);

    public Set<User> getAllModeratorsByCommunityId(Long communityId);

    public User getModeratorByCommunityAndUser(Long communityId, Long userId);

    public List<User> getModeratorCountFromCommunityById(Long id);

    public List<User> getUserCountFromCommunityById(Long id);

    @Modifying
    public void deleteCommunityById(Long id);

//    @Modifying
//    public void updateCommunityById(Long id, Community community);

}
