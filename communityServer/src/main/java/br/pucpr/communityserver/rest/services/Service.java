package br.pucpr.communityserver.rest.services;

import br.pucpr.communityserver.rest.users.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Service {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private User user;
}
