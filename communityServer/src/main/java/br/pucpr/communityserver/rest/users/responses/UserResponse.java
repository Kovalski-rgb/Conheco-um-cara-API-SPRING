package br.pucpr.communityserver.rest.users.responses;

import br.pucpr.communityserver.rest.users.User;
import br.pucpr.communityserver.rest.users.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class UserResponse {

    private Long id;

    public UserResponse(User user){
        this.id = user.getId();
    }

}
