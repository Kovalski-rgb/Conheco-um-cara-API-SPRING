package br.pucpr.productAndServiceserver.rest.products;

import br.pucpr.productAndServiceserver.rest.products.request.ProductRequest;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RequestMocks {

    public static ProductRequest getProductRequest(){
        return new ProductRequest(
                "productName",
                "productDescription",
                12.50,
                Stream.of("productType").collect(Collectors.toSet())
        );
    }

}
