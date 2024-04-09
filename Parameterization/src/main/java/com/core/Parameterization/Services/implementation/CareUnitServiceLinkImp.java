package com.core.Parameterization.Services.implementation;

import com.core.Parameterization.Entities.CareUnit;
import com.core.Parameterization.Entities.CareUnitServiceLink;
import com.core.Parameterization.Entities.ServiceEntity;
import com.core.Parameterization.Respositories.CareUnitServiceLinkRepo;
import com.core.Parameterization.Services.CareUnitServiceLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareUnitServiceLinkImp implements CareUnitServiceLinkService {
    @Autowired
    CareUnitServiceLinkRepo careUnitServiceLinkRepo;
    public List<CareUnitServiceLink> getCareUnitAndService(Integer iCareUnitKey , Long iServiceKey){
        return careUnitServiceLinkRepo.findByService_ServiceKyAndCareUnit_CareunitKey(iServiceKey,iCareUnitKey);

    }
    @Override
    public ServiceEntity getServiceByKey(Long iServiceKey){
        return careUnitServiceLinkRepo.findByService_ServiceKy(iServiceKey);
    }

    @Override
    public void deletService(CareUnitServiceLink iCareUnitServiceLink){
        careUnitServiceLinkRepo.delete(iCareUnitServiceLink);
    }
}
