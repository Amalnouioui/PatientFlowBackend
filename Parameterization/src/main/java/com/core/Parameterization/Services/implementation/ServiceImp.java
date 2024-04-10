package com.core.Parameterization.Services.implementation;

import com.core.Parameterization.Entities.CareUnitServiceLink;
import com.core.Parameterization.Entities.Enumeration.ServiceType;
import com.core.Parameterization.Entities.Enumeration.UnitType;
import com.core.Parameterization.Entities.ServiceEntity;
import com.core.Parameterization.Respositories.CareUnitServiceLinkRepo;
import com.core.Parameterization.Respositories.ServiceRepo;
import com.core.Parameterization.Services.CareUnitServiceLinkService;
import com.core.Parameterization.Services.ServiceSer;
import com.thoughtworks.xstream.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceImp implements ServiceSer {
    @Autowired
    ServiceRepo serviceRepo;
    @Autowired
    CareUnitServiceLinkService careUnitServiceLinkService;
    @Autowired
    CareUnitServiceLinkRepo careUnitServiceLinkRepo ;

    @Override
    public List<ServiceEntity> getServices() {

       return serviceRepo.findAll();
    }
    @Override
    public void addService (ServiceEntity service) {
        ServiceEntity aService=serviceRepo.findByServiceName(service.getServiceName());
        if(aService != null) {
            throw  new IllegalStateException("Le Nom de Service déja existe , donner un nom unique !");

        }
         serviceRepo.save(service);
    }

    @Override
    public Optional<ServiceEntity> getServiceById(Long serviceKey) {
        return  serviceRepo.findById(serviceKey);
    }

@Override
    public void updateService(Long iServiceId,ServiceEntity iService){

        ServiceEntity aServiceId=serviceRepo.findById(iServiceId).orElse(null);
        if(aServiceId!=null) {
            ServiceEntity aServiceName = serviceRepo.findByServiceName(iService.getServiceName());
            if ((aServiceName != null) && (aServiceName.getServiceKy() != aServiceId.getServiceKy())) {
                throw new IllegalStateException("Le nom de service déja existe !");
            }

             serviceRepo.save(iService);
        }else{
            throw new IllegalStateException("Le service n'existe pas !");
        }
}

@Override
public void deleteService(Long serviceId) {
    // Supprimer d'abord les associations dans care_unit_service_link
    careUnitServiceLinkRepo.deleteByService_ServiceKy(serviceId);

    // Ensuite, supprimer le service lui-même
    serviceRepo.deleteById(serviceId);
}



}







