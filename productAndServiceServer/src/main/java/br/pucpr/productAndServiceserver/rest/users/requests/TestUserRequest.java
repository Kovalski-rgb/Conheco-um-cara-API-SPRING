package br.pucpr.productAndServiceserver.rest.users.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestUserRequest {
    private String token;
}
