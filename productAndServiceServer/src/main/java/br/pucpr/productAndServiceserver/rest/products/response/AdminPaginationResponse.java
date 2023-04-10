package br.pucpr.productAndServiceserver.rest.products.response;

import br.pucpr.productAndServiceserver.rest.products.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class AdminPaginationResponse {

    private Integer pageNumber;
    private Integer lastPage;
    private List<Product> products;
}
