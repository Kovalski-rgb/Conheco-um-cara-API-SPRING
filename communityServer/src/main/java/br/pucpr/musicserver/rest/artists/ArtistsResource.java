package br.pucpr.musicserver.rest.artists;

import br.pucpr.musicserver.rest.album.Album;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistsResource {
    private ArtistsService service;

    public ArtistsResource(ArtistsService service) {
        this.service = service;
    }

    @GetMapping
    @Transactional
    public List<Artist> search(
        @RequestParam(value = "genre", required = false) String genre
    ) {
        return service.search(genre);
    }

    @PostMapping
    @Transactional
    @SecurityRequirement(name="AuthServer")
    @RolesAllowed({"ADMIN"})
    public Artist add(@Valid @RequestBody Artist artist) {
        return service.add(artist);
    }

    @DeleteMapping("{id}")
    @Transactional
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name="AuthServer")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }

    @GetMapping(path = "/{id}/albums")
    @Transactional
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name="AuthServer")
    public List<Album> getArtistAlbum(@PathVariable("id") Long id){
        return service.getAlbumsFromArtist(id);
    }
}
