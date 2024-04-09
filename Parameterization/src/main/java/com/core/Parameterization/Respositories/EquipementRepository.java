package com.core.Parameterization.Respositories;

import com.core.Parameterization.Entities.Enumeration.EquipmentType;
import com.core.Parameterization.Entities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipementRepository extends JpaRepository<Equipment,Long> {

List<Equipment>findEquipmentByEquipmentType(EquipmentType equipmentType);

}
