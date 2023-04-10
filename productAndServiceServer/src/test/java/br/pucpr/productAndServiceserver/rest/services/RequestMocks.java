package br.pucpr.productAndServiceserver.rest.services;

import br.pucpr.productAndServiceserver.rest.services.request.ServiceRequest;
import br.pucpr.productAndServiceserver.rest.services.request.UpdateServiceRequestDTO;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RequestMocks {

    public static ServiceRequest getServiceRequest(){
        return new ServiceRequest(
                "serviceName",
                "serviceDescription",
                12.50,
                Stream.of("serviceType").collect(Collectors.toSet())
        );
    }

    public static UpdateServiceRequestDTO getUpdateServiceRequestDTO(){
        return new UpdateServiceRequestDTO(
                "newServiceName",
                "newServiceDescription",
                15.20,
                Stream.of("newServiceType").collect(Collectors.toSet())
        );
    }

}
