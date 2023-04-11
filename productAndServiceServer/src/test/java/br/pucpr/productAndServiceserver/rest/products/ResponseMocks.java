package br.pucpr.productAndServiceserver.rest.products;

import br.pucpr.productAndServiceserver.rest.products.response.AdminProductPaginationResponse;
import br.pucpr.productAndServiceserver.rest.products.response.ProductPaginationResponse;
import br.pucpr.productAndServiceserver.rest.products.response.ProductResponse;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResponseMocks {

    public static ProductResponse getProductResponse(){
        return new ProductResponse(
                1L,
                "productName",
                "productDescription",
                12.50,
                LocalDateTime.MIN
        );
    }

    public static AdminProductPaginationResponse getAdminPaginationResponse(){
        return new AdminProductPaginationResponse(
                0,
                0,
                Stream.of(ProductMocks.getProduct()).collect(Collectors.toList())
        );
    }

    public static ProductPaginationResponse getProductPaginationResponse(){
        return new ProductPaginationResponse(
                0,
                0,
                Stream.of(new ProductResponse(ProductMocks.getProduct())).collect(Collectors.toList())
        );
    }

}
