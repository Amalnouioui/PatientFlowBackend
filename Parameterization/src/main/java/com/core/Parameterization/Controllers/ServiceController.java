package com.core.Parameterization.Controllers;

import com.core.Parameterization.Entities.Equipment;
import com.core.Parameterization.Entities.ServiceEntity;
import com.core.Parameterization.Services.ServiceSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/parameterization/Service")
public class ServiceController {
    @Autowired
    private ServiceSer serviceSer;

    @GetMapping
    public ResponseEntity<List<ServiceEntity>> getAllServices() {
        List<ServiceEntity> aServiceEntities = serviceSer.getServices();
        return new ResponseEntity<>(aServiceEntities, HttpStatus.OK);
    }

    //Get service by id
    @GetMapping("/{serviceId}")
    public ResponseEntity<Optional<ServiceEntity>> getAllServices(@PathVariable ("serviceId") Long iServiceId) {

        Optional<ServiceEntity> aServiceEntities = serviceSer.getServiceById(iServiceId);
        if(aServiceEntities.isPresent()){
        return new ResponseEntity<>(aServiceEntities, HttpStatus.OK);
    }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}
    // Add a new service
    @PostMapping
    public String addService(@RequestBody ServiceEntity iService) {
        try{
             serviceSer.addService(iService);
             return  ("la service a été ajouter avec succées ");
        }catch(Exception e){
            return (e.getMessage());
        }

    }
    // Update an existing equipment
    @PutMapping("/{id}")
    public String updateService(@PathVariable("id") Long iServiceId, @RequestBody ServiceEntity iService) {
        try {
            iService.setServiceKy(iServiceId);
           serviceSer.updateService(iServiceId,iService);
            return ("Le service a été mise a jour avec succées ");
        }catch(Exception e){
            return (e.getMessage());
        }


    }

    // Delete an equipment
    @DeleteMapping("/{id}")
    public String deletService(@PathVariable("id") Long iServiceId) {
        Optional<ServiceEntity> aExistingService = serviceSer.getServiceById(iServiceId);
        if (aExistingService.isPresent()) {
            serviceSer.deleteService(iServiceId);
            return (" Ler service a été supprimer avec succées ");
        } else {
            return ("Le service n'existe pas !");
        }
    }
    
 // Méthode pour récupérer les services où isSurgeryService est true
    @GetMapping("/surgery")
    public ResponseEntity<List<ServiceEntity>> getServicesForSurgery() {
        List<ServiceEntity> servicesForSurgery = serviceSer.getServicesBySurgeyTrue();
        return new ResponseEntity<>(servicesForSurgery, HttpStatus.OK);
    }

    // Méthode pour récupérer les services où isSurgeryService est false
    @GetMapping("/not-surgery")
    public ResponseEntity<List<ServiceEntity>> getServicesNotForSurgery() {
        List<ServiceEntity> servicesNotForSurgery = serviceSer.getServicesBySurgeyFalse();
        return new ResponseEntity<>(servicesNotForSurgery, HttpStatus.OK);
    }
}
