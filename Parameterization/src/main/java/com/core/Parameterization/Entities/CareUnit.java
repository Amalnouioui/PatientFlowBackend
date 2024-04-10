package com.core.Parameterization.Entities;

import com.core.Parameterization.Entities.Enumeration.UnitStatus;
import com.core.Parameterization.Entities.Enumeration.UnitType;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.Calendar;
import java.util.Locale;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "careunitKey")

@Table(name = "CareUnit")



public class CareUnit {

    @Id
    @Column(name="CareUnit_key", nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //Care Unit primary key
    private Integer careunitKey;
    //Care unit DATA FIELDS
    @Getter
    @Column(name="CareUnit_Name", nullable=false)
    private String careunitName;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name="CareUnit_Status", nullable=false)
    private UnitStatus careunitStatue;

    @Getter
    @Column(name="careUnit_Capacity",nullable = false)
    private int careunitCapacity;

    @Getter
    @Column(name="CareUnit_StartTime",nullable=false)
    private Timestamp careUnit_StartTime;

    @Getter
    @Column(name="CareUnit_EndTime",nullable=false)
    private Timestamp careUnit_EndTime;

    @Transient
    private String operationhoure;

    @Column(name="CareUnit_Desc", nullable=false)
    private String careunitDescription;

    @Column(name="CareUnit_Head", nullable=false)
    private String careunitResponsable;

    @Column(name="CareUnit_Type", nullable=false)
    @Enumerated(EnumType.STRING) // ou EnumType.ORDINAL, selon vos besoins
    private UnitType careuniType;
    @Transient
    @JsonIgnore
    private List<Long> equipmentList;


    @JsonIgnore
    @Transient
    private List<Long> deletedEquipmentIds;






    //@JsonIgnore
    @Getter
    @OneToMany(mappedBy = "careUnit",cascade = CascadeType.ALL)
    Set<CareUnitEquipLink> careUnitEquipLinkSet;

    @ManyToOne
    @JoinColumn(name = "Service_Ref", referencedColumnName = "Service_Ky")
    @JsonIdentityReference(alwaysAsId = true)
    private  ServiceEntity serviceCareunits;


    public ServiceEntity getServiceCareunits() {
        return serviceCareunits;
    }

    public void setServiceCareunits(ServiceEntity serviceCareunits) {
        this.serviceCareunits = serviceCareunits;
    }

    @OneToMany(mappedBy = "careunitRoom",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Room>rooms;
    public Integer getCareunitKey() {
        return careunitKey;
    }

    public void setCareunitKey(Integer careunitKey) {
        this.careunitKey = careunitKey;
    }

    public void setCareunitName(String careunitName) {
        this.careunitName = careunitName;
    }

    public void setCareunitStatue(UnitStatus careunitStatue) {
        this.careunitStatue = careunitStatue;
    }

    public void setCareunitCapacity(int careunitCapacity) {
        this.careunitCapacity = careunitCapacity;
    }

    public void setCareUnit_StartTime(Timestamp careUnit_StartTime) {
        this.careUnit_StartTime = careUnit_StartTime;
    }

    public void setCareUnit_EndTime(Timestamp careUnit_EndTime) {
        this.careUnit_EndTime = careUnit_EndTime;
    }

    public String getOperationhoure() {
        return hours(this.careUnit_StartTime, this.careUnit_EndTime);
    }

    public static String hours(Timestamp startTime, Timestamp endTime) {
        // Convertir les Timestamps en objets Calendar
        Calendar startCal = Calendar.getInstance();
        startCal.setTimeInMillis(startTime.getTime());
        Calendar endCal = Calendar.getInstance();
        endCal.setTimeInMillis(endTime.getTime());

        // Formatter les dates pour extraire les jours et les heures
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.FRENCH);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mma", Locale.getDefault()); // Utilisation de l'horloge de 12 heures avec am/pm

        String startDay = dayFormat.format(startCal.getTime());
        String startTimeStr = timeFormat.format(startCal.getTime());

        String endDay = dayFormat.format(endCal.getTime());
        String endTimeStr = timeFormat.format(endCal.getTime());

        // Concaténer les informations dans le format désiré
        return startDay + "A" + endDay + "_" + startTimeStr + "_" + endTimeStr;
    }

    public void setOperationhoure(String operationhoure) {
        this.operationhoure=operationhoure;

    }

    public String getCareunitDescription() {
        return careunitDescription;
    }

    public void setCareunitDescription(String careunitDescription) {
        this.careunitDescription = careunitDescription;
    }

    public String getCareunitResponsable() {
        return careunitResponsable;
    }

    public void setCareunitResponsable(String careunitResponsable) {
        this.careunitResponsable = careunitResponsable;
    }

    public UnitType getCareuniType() {
        return careuniType;
    }

    public void setCareuniType(UnitType careuniType) {
        this.careuniType = careuniType;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setCareUnitEquipLinkSet(Set<CareUnitEquipLink> careUnitEquipLinkSet) {
        this.careUnitEquipLinkSet = careUnitEquipLinkSet;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    //@JsonIgnore


    public Set<CareUnitEquipLink> getCareUnitEquipLinkSet() {
        return careUnitEquipLinkSet;
    }



    public String getCareunitName() {
        return careunitName;
    }

    public UnitStatus getCareunitStatue() {
        return careunitStatue;
    }
    public int getCareunitCapacity() {
        return careunitCapacity;
    }

    public Timestamp getCareUnit_StartTime() {
        return careUnit_StartTime;
    }

    public Timestamp getCareUnit_EndTime() {
        return careUnit_EndTime;
    }

    public List<Long> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<Long> equipmentList) {
        this.equipmentList = equipmentList;
    }


    public List<Long> getDeletedEquipmentIds() {
        return deletedEquipmentIds;
    }

    public void setDeletedEquipmentIds(List<Long> deletedEquipmentIds) {
        this.deletedEquipmentIds = deletedEquipmentIds;
    }


}