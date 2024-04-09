package com.core.Parameterization.Services.implementation;

import com.core.Parameterization.Entities.BedEquipLink;
import com.core.Parameterization.Respositories.BedEquiLinkRepo;
import com.core.Parameterization.Services.BedEquipLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BedEquipLinkImp implements BedEquipLinkService {
    @Autowired
    BedEquiLinkRepo bedEquiLinkRepo;
    @Override
    public void deleteEquipmentBed(BedEquipLink bedEquipLink){
        bedEquiLinkRepo.delete(bedEquipLink);
    }
}
