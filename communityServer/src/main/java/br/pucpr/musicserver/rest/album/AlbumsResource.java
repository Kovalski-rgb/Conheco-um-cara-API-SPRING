package br.pucpr.musicserver.rest.album;

import br.pucpr.musicserver.rest.artists.Artist;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/albums")
public class AlbumsResource {

    private AlbumsService service;

    public AlbumsResource(AlbumsService service) { this.service = service; }

    @PostMapping
    @Transactional
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name="AuthServer")
    public Album add(@Valid @RequestBody Album album){
        return service.add(album);
    }

    @DeleteMapping("{id}")
    @Transactional
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name="AuthServer")
    public void delete(@PathVariable("id") Long id) { service.delete(id); }

    @GetMapping("/search")
    @Transactional
    @RolesAllowed({"USER", "ADMIN"})
    @SecurityRequirement(name="AuthServer")
    public List<Album> search(
        @RequestParam(value = "artist", required = false) Long id,
        @RequestParam(value = "to", required = false) Long to,
        @RequestParam(value = "from", required = false) Long from,
        @RequestParam(value = "genre", required = false) String genre
    ){
        return service.search(id, to, from, genre);
    }

}
