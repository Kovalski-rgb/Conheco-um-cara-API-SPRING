package br.pucpr.productAndServiceserver.rest.services.response;

import br.pucpr.productAndServiceserver.rest.services.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class AdminServicePaginationResponse {

    private Integer pageNumber;
    private Integer lastPage;
    private List<Service> services;
}
