package br.pucpr.productAndServiceserver.rest.products;

import br.pucpr.productAndServiceserver.rest.products.response.AdminPaginationResponse;
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
                Stream.of("productType").collect(Collectors.toSet()),
                LocalDateTime.MIN
        );
    }

    public static AdminPaginationResponse getAdminPaginationResponse(){
        return new AdminPaginationResponse(
                0,
                0,
                Stream.of(DataMocks.getProduct()).collect(Collectors.toList())
        );
    }

    public static ProductPaginationResponse getProductPaginationResponse(){
        return new ProductPaginationResponse(
                0,
                0,
                Stream.of(new ProductResponse(DataMocks.getProduct())).collect(Collectors.toList())
        );
    }

}
