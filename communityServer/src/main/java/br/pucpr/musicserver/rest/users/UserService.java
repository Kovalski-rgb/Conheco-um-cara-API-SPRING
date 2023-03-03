package br.pucpr.musicserver.rest.users;

import br.pucpr.musicserver.lib.security.JWT;
import br.pucpr.musicserver.rest.users.requests.LoginRequest;
import br.pucpr.musicserver.rest.users.resonses.UserLoginResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class UserService {

    private JWT jwt;

    public UserService(JWT jwt) {
        this.jwt = jwt;
    }

    public UserLoginResponse createTestUser(String token){
        return jwt.createTestUser(token);
    }

    public UserLoginResponse login(LoginRequest credentials){
        var api = new RestTemplate();
        var uri = new DefaultUriBuilderFactory()
                .builder()
                .scheme("http")
                .host("localhost")
                .port(3001)
                .path("/api/users/login")
                .build();
        var headers = new HttpHeaders();

//        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+token);
        var request = new HttpEntity<>(credentials/*, headers*/);

        var response = api.exchange(
                uri,
                HttpMethod.POST,
                request,
                UserLoginResponse.class
                );

        return response.getStatusCode().is2xxSuccessful() ?
                response.getBody() : null;
    }

}
