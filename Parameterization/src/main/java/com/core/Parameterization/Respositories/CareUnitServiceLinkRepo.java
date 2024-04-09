package com.core.Parameterization.Respositories;

import com.core.Parameterization.Entities.CareUnit;
import com.core.Parameterization.Entities.CareUnitServiceLink;
import com.core.Parameterization.Entities.ServiceEntity;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareUnitServiceLinkRepo extends JpaRepository<CareUnitServiceLink, Long> {
    List<CareUnitServiceLink> findByService_ServiceKyAndCareUnit_CareunitKey(Long serviceKey, Integer careUnitKey);

    CareUnit findByCareUnit_CareunitKey(Integer careunitKey);
    ServiceEntity findByService_ServiceKy(Long serviceKey);
    
    Optional <CareUnitServiceLink>findCareUnitServiceLinkByService_ServiceKy(Long serviceKey);
    @Transactional
    @Modifying
    void deleteByService_ServiceKy(Long serviceId);


}
