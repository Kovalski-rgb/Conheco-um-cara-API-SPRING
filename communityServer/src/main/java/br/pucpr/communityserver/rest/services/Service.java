package br.pucpr.communityserver.rest.services;

import br.pucpr.communityserver.rest.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data @Entity
@AllArgsConstructor
@NoArgsConstructor
public class Service {

    @Id
    private Long id;

    @NotNull
    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private User owner;

}
