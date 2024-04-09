package com.core.Parameterization.Services;

import com.core.Parameterization.Entities.Equipment;

import java.util.List;
import java.util.Optional;

public interface EquipmentService {

 List<Equipment> getAllEquipments();
 Optional<Equipment> getEquipmentById(Long equipmentId);
 Equipment addEquipment(Equipment equipment);
 Equipment updateEquipment(Equipment equipment);
 void deleteEquipment(Long equipmentId);
 List<Equipment>getCareunitEquipment();
 List<Equipment> getBedEquipment();
}
