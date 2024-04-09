package com.core.Parameterization.Services.implementation;

import com.core.Parameterization.Entities.CareUnitEquipLink;
import com.core.Parameterization.Respositories.CareUnitEquipLinkRepo;
import com.core.Parameterization.Services.CareUnitEquipLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CareUnitEquipLinkImp implements CareUnitEquipLinkService {
    @Autowired
    CareUnitEquipLinkRepo careUnitEquipLinkRepo ;
    @Override
  public   void deleteEquipement (CareUnitEquipLink iCareUnitEquipLink){
        careUnitEquipLinkRepo.delete(iCareUnitEquipLink);

    }

}
