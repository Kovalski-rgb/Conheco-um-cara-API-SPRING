package br.pucpr.productAndServiceserver.rest.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    public List<Service> getServicesByOwnerId(Long ownerId, PageRequest pageable);
    public List<Service> selectAllServices(PageRequest pageable);
    public int countAllServices();
    public int countServicesByOwnerId(Long ownerId);



}
