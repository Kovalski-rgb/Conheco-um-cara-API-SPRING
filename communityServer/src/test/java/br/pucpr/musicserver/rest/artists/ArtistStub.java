package br.pucpr.musicserver.rest.artists;

import java.util.Random;
import java.util.Set;

public class ArtistStub {

	static Random random = new Random();
	
	public static String randomString(int size) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < size; i++) {
			s.append((char) (random.nextInt(52)+'A'));
		}
		return s.toString();
	}
	
	public static Artist artistStub(Long id) {
		return new Artist
				(id, randomString(10), Set.of(randomString(10), randomString(10)));
	}
	
}
