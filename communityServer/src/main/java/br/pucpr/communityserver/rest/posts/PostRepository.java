package br.pucpr.communityserver.rest.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    public List<Post> getAllPostsFromUserById(Long id);

    public List<Post> getAllPostsFromUserByCommunityById(Long communityId, Long userId);

    public List<Post> getAllPostsFromCommunityById(Long communityId);

    @Modifying
    public void deletePostById(Long postId);

}
