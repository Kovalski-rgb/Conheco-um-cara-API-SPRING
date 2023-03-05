package br.pucpr.communityserver.rest.posts;

import br.pucpr.communityserver.rest.communities.Community;
import br.pucpr.communityserver.rest.products.Product;
import br.pucpr.communityserver.rest.services.Service;
import br.pucpr.communityserver.rest.users.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;

    @NotNull
    private User creator;
    private Product product;
    private Service service;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    @NotNull
    private Community community;

}
