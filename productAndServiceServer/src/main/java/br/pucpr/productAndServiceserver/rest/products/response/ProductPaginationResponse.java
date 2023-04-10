package br.pucpr.productAndServiceserver.rest.products.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class ProductPaginationResponse {

    private Integer pageNumber;
    private Integer lastPage;
    private List<ProductResponse> products;

}
