package br.pucpr.musicserver.rest.album;

import br.pucpr.musicserver.rest.artists.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumsRepository extends JpaRepository<Album, Long> {

    List<Album> getAlbumsFromArtistById(Long id);

    List<Album> getAlbumsFromArtistByGenre(String genre);

}
