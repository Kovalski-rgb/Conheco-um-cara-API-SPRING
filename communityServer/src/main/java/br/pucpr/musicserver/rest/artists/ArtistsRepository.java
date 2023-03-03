package br.pucpr.musicserver.rest.artists;

import br.pucpr.musicserver.rest.album.Album;
import jakarta.persistence.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistsRepository extends JpaRepository<Artist, Long> {

    List<Artist> findByGenre(String genre);

    List<Album> getAlbumsFromArtistById(Long id);
}
