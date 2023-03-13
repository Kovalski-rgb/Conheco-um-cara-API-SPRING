package br.pucpr.communityserver.rest.communities;

import br.pucpr.communityserver.rest.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    public Community getCommunitiesByCodeAndName(String name, String code);

    public User getUserInCommunityById(Long communityId, Long userId);

//    public void insertNewUserIntoCommunity(Long communityId, Long userId);

    @Modifying
    public void removeUserFromCommunity(Long communityId, Long userId);

    public List<Community> getAllCommunitiesByUserId(Long userId);

}
