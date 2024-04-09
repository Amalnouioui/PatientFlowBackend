package com.core.Parameterization.Respositories;

import com.core.Parameterization.Entities.ServiceEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepo extends JpaRepository<ServiceEntity,Long> {
    ServiceEntity findByServiceName(String serviceName);
 // Méthode pour récupérer les services où isSurgeryService est true
    List<ServiceEntity> findByIsSurgeryServiceTrue();

    // Méthode pour récupérer les services où isSurgeryService est false
    List<ServiceEntity> findByIsSurgeryServiceFalse();

}
