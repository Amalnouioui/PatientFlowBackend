package com.core.Parameterization.Services;

import com.core.Parameterization.Entities.Equipment;
import com.core.Parameterization.Entities.ServiceEntity;

import java.util.List;
import java.util.Optional;

public interface ServiceSer {
    List<ServiceEntity> getServices();
    void addService(ServiceEntity service);
    Optional <ServiceEntity> getServiceById(Long serviceKey);
   void  updateService(Long iServiceId,ServiceEntity service);
    void deleteService(Long iServiceId);
    List<ServiceEntity> getServicesBySurgeyTrue();
    List<ServiceEntity> getServicesBySurgeyFalse();


    
}
