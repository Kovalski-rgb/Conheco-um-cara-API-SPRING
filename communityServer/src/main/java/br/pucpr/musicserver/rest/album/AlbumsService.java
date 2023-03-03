package br.pucpr.musicserver.rest.album;

import br.pucpr.musicserver.lib.exception.BadRequestException;
import br.pucpr.musicserver.lib.exception.NotFoundException;
import br.pucpr.musicserver.rest.artists.Artist;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AlbumsService {

    private AlbumsRepository repository;

    public AlbumsService(AlbumsRepository repository) { this.repository = repository; }

    public Album add(Album album){
        if(album==null) {
            throw new BadRequestException("Album cannot be null");
        }
        if(album.getId()!=null) {
            throw new BadRequestException("Album cannot have a preset id");
        }
        return repository.save(album);
    }

    public void delete(Long id){
        if( !repository.existsById(id)){
            throw new NotFoundException(id);
        }
        repository.deleteById(id);
    }

    public List<Album> search(Long id, Long to, Long from, String genre){
        List<Album> albums;
        if(genre != null){
            return repository.getAlbumsFromArtistByGenre(genre);
        }

        if(id == null || !repository.existsById(id)){
            albums = repository.findAll(Sort.by(Sort.Order.asc("name")));
        } else {
            albums = repository.getAlbumsFromArtistById(id);
        }

        int cont = 0;

        if(to != null || from != null) {
            while (albums.size() > cont) {
                Album album = albums.get(cont);
                boolean delete = false;

                if (to != null) {
                    if (album.getReleaseYear() > to) {
                        delete = true;
                    }
                }

                if (from != null) {
                    if (album.getReleaseYear() < from) {
                        delete = true;
                    }
                }

                if (delete) {
                    albums.remove(cont);
                }
                cont++;
            }
        }

        return albums;
    }

}
