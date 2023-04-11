package br.pucpr.productAndServiceserver.rest.products.response;

import br.pucpr.productAndServiceserver.rest.products.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class AdminProductPaginationResponse {

    @Schema(example = "0")
    private Integer pageNumber;
    @Schema(example = "3")
    private Integer lastPage;
    private List<Product> products;
}
