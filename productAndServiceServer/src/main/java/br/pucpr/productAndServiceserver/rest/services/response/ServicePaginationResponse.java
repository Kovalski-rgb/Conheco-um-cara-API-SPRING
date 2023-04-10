package br.pucpr.productAndServiceserver.rest.services.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class ServicePaginationResponse {

    private Integer pageNumber;
    private Integer lastPage;
    private List<ServiceResponse> services;

}
