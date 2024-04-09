package com.core.Parameterization.Services.implementation;

import com.core.Parameterization.Entities.BedEquipLink;
import com.core.Parameterization.Entities.CareUnitEquipLink;
import com.core.Parameterization.Entities.Enumeration.EquipmentType;
import com.core.Parameterization.Entities.Equipment;
import com.core.Parameterization.Respositories.BedEquiLinkRepo;
import com.core.Parameterization.Respositories.CareUnitEquipLinkRepo;
import com.core.Parameterization.Respositories.EquipementRepository;
import com.core.Parameterization.Services.BedEquipLinkService;
import com.core.Parameterization.Services.CareUnitEquipLinkService;
import com.core.Parameterization.Services.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipementRepository equipmentRepository;
    @Autowired
    private CareUnitEquipLinkRepo careUnitEquipLinkRepo;
    @Autowired
    private CareUnitEquipLinkService careUnitEquipLinkService ;
    @Autowired
    private BedEquiLinkRepo bedEquiLinkRepo;
    @Autowired
    private BedEquipLinkService bedEquipLinkService;

    @Override
    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    @Override
    public Optional<Equipment> getEquipmentById(Long equipmentId) {
        return equipmentRepository.findById(equipmentId);
    }

    @Override
    public Equipment addEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    @Override
    public Equipment updateEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    @Override
    public void deleteEquipment(Long equipmentId) {
        // Recherche des associations avec les unités de soins
        List<CareUnitEquipLink> careUnitAssociations = careUnitEquipLinkRepo.findByEquipment_Equipementkey(equipmentId);

        // Recherche des associations avec les lits
        List<BedEquipLink> bedAssociations = bedEquiLinkRepo.findByEquipment_Equipementkey(equipmentId);

        // Supprimer les associations avec les unités de soins
        for (CareUnitEquipLink association : careUnitAssociations) {
            careUnitEquipLinkRepo.delete(association);
        }

        // Supprimer les associations avec les lits
        for (BedEquipLink association : bedAssociations) {
            bedEquiLinkRepo.delete(association);
        }

        // Supprimer l'équipement lui-même
        equipmentRepository.deleteById(equipmentId);
    }


    @Override
   public  List<Equipment>getCareunitEquipment(){
        EquipmentType aEquipmentType=EquipmentType.Unité_de_soins;
        return equipmentRepository.findEquipmentByEquipmentType(aEquipmentType);
    }
    @Override
    public  List<Equipment>getBedEquipment(){
        EquipmentType aEquipmentType=EquipmentType.Lit;
        return equipmentRepository.findEquipmentByEquipmentType(aEquipmentType);
    }
}
