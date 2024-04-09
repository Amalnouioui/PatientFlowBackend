package com.core.Parameterization.Entities;

import com.core.Parameterization.Entities.CareUnit;
import com.core.Parameterization.Entities.Equipment;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.primitives.UnsignedInteger;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity


@Table(name = "CareUnitEquipLink")
public class CareUnitEquipLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CareUnitEquipLink_Ky;

    public Long getCareUnitEquipLink_Ky() {
        return CareUnitEquipLink_Ky;
    }

    public void setCareUnitEquipLink_Ky(Long careUnitEquipLink_Ky) {
        CareUnitEquipLink_Ky = careUnitEquipLink_Ky;
    }

    public CareUnit getCareUnit() {
        return careUnit;
    }

    public void setCareUnit(CareUnit careUnit) {
        this.careUnit = careUnit;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    @ManyToOne
    @JoinColumn(name = "CareUnitEquipLink_PrntCareUnitKy")

    private CareUnit careUnit;

    @ManyToOne
    @JoinColumn(name = "CareUnitEquipLink_PrntEquipmentKy")

    private Equipment equipment;


}
