package br.pucpr.productAndServiceserver.rest.products.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class ProductPaginationResponse {

    @Schema(example = "0")
    private Integer pageNumber;
    @Schema(example = "3")
    private Integer lastPage;
    private List<ProductResponse> products;

}
