package br.pucpr.communityserver.rest.communities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {


    public Community getCommunitiesByCodeAndName(String name, String code);
    public List<String> getCommunitiesByCode(String code);

}
