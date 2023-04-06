package br.pucpr.communityserver.rest.communities;

import br.pucpr.communityserver.rest.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    /**
     * Returns a community that matches passed Name and Code <br>
     * Lazily loads User and Moderator lists
     * @return Null if no communities match the criteria
     */
    public Community getCommunitiesByCodeAndName(String name, String code);

    /**
     * Returns a specific user inside a Community that matches
     * @return Null if no communities match the criteria
     */
    public User getUserInCommunityById(Long communityId, Long userId);

    /**
     * Returns a community that matches the passed ID <br>
     * Eagerly loads User and Moderator lists
     * @return Null if no communities match the criteria
     */
    public Community getCommunityById(Long id);

    /**
     * Returns a list of communities that are associated with the user <br>
     * Lazily loads User and Moderator lists
     * @return Null if no communities match the criteria
     */
    public List<Community> getAllCommunitiesByUserId(Long userId);

    /**
     * Returns a user that is a moderator inside specified community
     * @return Null if no User matches the criteria
     */
    public User getModeratorByCommunityAndUser(Long communityId, Long userId);

    public List<User> getModeratorListFromCommunityById(Long id);

    public List<User> getUserListFromCommunityById(Long id);

    @Modifying
    public void deleteCommunityById(Long id);

//    @Modifying
//    public void updateCommunityById(Long id, Community community);

}
