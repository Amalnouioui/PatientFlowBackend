package com.core.Parameterization.Respositories;

import com.core.Parameterization.Entities.CareUnitEquipLink;
import com.core.Parameterization.Entities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface CareUnitEquipLinkRepo extends JpaRepository<CareUnitEquipLink,Long> {


    List<CareUnitEquipLink> findByEquipment_Equipementkey (Long equipmentKey);
    List<CareUnitEquipLink> findByCareUnit_CareunitKeyAndEquipment_Equipementkey(Integer careUnitId, Long equipmentId);


}
