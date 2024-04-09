package com.core.Parameterization.Services;

import com.core.Parameterization.Entities.CareUnit;
import com.core.Parameterization.Entities.CareUnitServiceLink;
import com.core.Parameterization.Entities.ServiceEntity;

import java.util.List;

public interface CareUnitServiceLinkService {
     List<CareUnitServiceLink> getCareUnitAndService(Integer iCareunitKey , Long iServiceKey);
     ServiceEntity getServiceByKey(Long iServiceKey);
     void deletService(CareUnitServiceLink careUnitServiceLink);
}
