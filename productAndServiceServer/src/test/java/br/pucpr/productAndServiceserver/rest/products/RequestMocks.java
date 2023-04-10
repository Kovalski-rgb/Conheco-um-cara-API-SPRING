package br.pucpr.productAndServiceserver.rest.products;

import br.pucpr.productAndServiceserver.rest.products.request.ProductRequest;
import br.pucpr.productAndServiceserver.rest.products.request.UpdateProductRequestDTO;

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

    public static UpdateProductRequestDTO getUpdateProductRequestDTO(){
        return new UpdateProductRequestDTO(
                "newProductName",
                "newProductDescription",
                15.20,
                Stream.of("newProductType").collect(Collectors.toSet())
        );
    }

}
