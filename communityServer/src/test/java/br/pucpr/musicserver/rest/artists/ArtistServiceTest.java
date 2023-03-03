package br.pucpr.musicserver.rest.artists;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.pucpr.musicserver.lib.exception.BadRequestException;

public class ArtistServiceTest {

	private ArtistsRepository repository;
	private ArtistsService service;

	@BeforeEach
	public void setup() {
		repository = mock(ArtistsRepository.class);
		service = new ArtistsService(repository);
	}

	@Test
	public void add_should_call_save_if_has_an_artist() {
		var artist = new Artist();
		artist.setName("Artist");
		artist.setGenres(Set.of("Artist"));
		
		var savedArtist = new Artist();
		savedArtist.setId(1L);
		savedArtist.setName("Artist");
		savedArtist.setGenres(Set.of("Artist"));

		when(repository.save(any())).thenReturn(savedArtist);

		var ret = service.add(artist);
		assertNotNull(ret.getId());
		assertEquals(savedArtist, ret);
	}

	@Test
	public void should_throw_badRequest_when_null() {
		assertThrows(BadRequestException.class, () -> {
			service.add(null);
		});
	}

	@Test
	public void should_throw_badRequest_when_id_not_null() {
		var artist = ArtistStub.artistStub(1L);
		assertThrows(BadRequestException.class, () -> {
			service.add(artist);
		});
	}
	
}
