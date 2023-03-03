package br.pucpr.musicserver.rest.artists;

import br.pucpr.musicserver.rest.album.Album;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.core.util.Json;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.cdi.Eager;

import java.util.Set;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@NamedQuery(
    name="Artist.findByGenre",
    query="SELECT a FROM Artist a" +
            " JOIN a.genres g" +
            " WHERE g = :genre" +
            " ORDER BY a.name"
)
@NamedQuery(
    name="Artist.getAlbumsFromArtistById",
    query="SELECT albums FROM Artist a" +
            " JOIN a.albums alb" +
            " WHERE a.id = :id" +
            " ORDER BY alb.name"
)
public class Artist {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @ElementCollection
    @CollectionTable(name="Genres", joinColumns = @JoinColumn(name="id"))
    @Column(name="genre")
    @NotEmpty
    private Set<String> genres;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(
//            fetch = FetchType.EAGER,
            mappedBy = "creators"
    )
    private Set<Album> albums;

    public Artist(Long id, String name, Set<String> genres){
        this.id = id;
        this.name = name;
        this.genres = genres;
    }
}
