package com.core.Parameterization.Respositories;

import com.core.Parameterization.Entities.BedEquipLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BedEquiLinkRepo extends JpaRepository<BedEquipLink,Long> {
    List<BedEquipLink> findByBed_BedKeyAndEquipment_Equipementkey(Integer bedKey, Long equipmentKey);
    List<BedEquipLink>findByEquipment_Equipementkey(Long equipmentKey);
}
