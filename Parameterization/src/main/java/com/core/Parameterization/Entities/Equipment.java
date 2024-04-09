package com.core.Parameterization.Entities;

import com.core.Parameterization.Entities.Enumeration.EquipmentType;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public  class Equipment {
    @Id
    @Column(name="Equipment_Key",nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipementkey;

    @Column(name="Equipment_Name", nullable=false)
    private String equipmentName ;
    @Enumerated(EnumType.STRING)

    //@JsonIgnore
    @Column(name="Equipment_Type",nullable=false)
    private EquipmentType equipmentType ;


    @JsonIgnore
    @OneToMany(mappedBy = "equipment")
    Set<CareUnitEquipLink> careUnitEquipLinkSet;

    @JsonIgnore
    @OneToMany(mappedBy = "equipment")
    Set<BedEquipLink>bedEquipLinkSet;



}
