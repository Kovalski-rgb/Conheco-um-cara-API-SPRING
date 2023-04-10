package br.pucpr.productAndServiceserver.rest.services;

import br.pucpr.productAndServiceserver.rest.services.response.AdminServicePaginationResponse;
import br.pucpr.productAndServiceserver.rest.services.response.ServicePaginationResponse;
import br.pucpr.productAndServiceserver.rest.services.response.ServiceResponse;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResposeMocks {

    public static ServiceResponse getServiceResponse(){
        return new ServiceResponse(
                1L,
                "serviceName",
                "serviceDescription",
                12.50,
                Stream.of("serviceType").collect(Collectors.toSet()),
                LocalDateTime.MIN
        );
    }

    public static AdminServicePaginationResponse getAdminServicePaginationResponse(){
        return new AdminServicePaginationResponse(
                0,
                0,
                Stream.of(ServiceMocks.getService()).collect(Collectors.toList())
        );
    }

    public static ServicePaginationResponse getServicePaginationResponse(){
        return new ServicePaginationResponse(
                0,
                0,
                Stream.of(new ServiceResponse(ServiceMocks.getService())).collect(Collectors.toList())
        );
    }

}
