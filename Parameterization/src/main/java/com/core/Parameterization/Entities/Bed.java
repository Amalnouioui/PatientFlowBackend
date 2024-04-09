package com.core.Parameterization.Entities;

import com.core.Parameterization.Entities.Enumeration.BedPhysicalCondition;
import com.core.Parameterization.Entities.Enumeration.BedStatus;
import com.core.Parameterization.Entities.Enumeration.BedType;
import com.core.Parameterization.Entities.Enumeration.BedCleaningStatus;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bedKey")
@Table(name = "Bed")
public class Bed {
    //--- BED PRIMARY KEYS
    @Id
    @Column(name="Bed_Key", nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bedKey;//changement de type



    @Column(name="Bed_Number", nullable=false)
    private Integer bedNumber; //String



    @Enumerated(EnumType.STRING) // Mapping the enum to a String column
    @Column(name="Bed_Type", nullable=false)
    private BedType bedType;



    @Enumerated(EnumType.STRING)
    @Column(name = "Bed_Status", nullable = false)
    private BedStatus bedStatue;



    @Column(name="Bed_Description", nullable=false)
    private String bedDescription;


    @Enumerated(EnumType.STRING)
    @Column(name="Physical_PhysicalCondition", nullable=false) //Enum
    private BedPhysicalCondition physicalState;


    @Column(name="Bed_PurchaseDate", nullable=false)
    private Timestamp bedPurchaseDate;


    @Enumerated(EnumType.STRING)
    @Column(name="Bed_CleaningStatus", nullable=false)
    private BedCleaningStatus bedCleaningStatus;


    @Column(name="Expiration_Date", nullable=false)
    private Timestamp expirationDate;


    @Transient
    private Integer RemainingLifeSpan;



    @Column(name="Bed_MaxWeightCapacity", nullable=false)
    private Integer poids;



    @ManyToOne
    @JoinColumn(name="roomBed", referencedColumnName = "Room_Key")
    @JsonIdentityReference(alwaysAsId = true)
    private Room roomBed;

    //@JsonIgnore
    @OneToMany(mappedBy = "bed",cascade = CascadeType.ALL)
    Set<BedEquipLink>bedEquipLinkSet;

    @JsonIgnore
    @Transient
    private List<Long> equipmentList;

    public List<Long> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<Long> equipmentIds) {
        this.equipmentList = equipmentIds;
    }
    @JsonIgnore
    @Transient
    private List<Long> deletedEquipmentIds;
    
  
    

    public Integer getBedKey() {
        return bedKey;
    }

    public void setBedKey(Integer bedKey) {
        this.bedKey = bedKey;
    }

    public Integer getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(Integer bedNumber) {
        this.bedNumber = bedNumber;
    }

    public BedType getBedType() {
        return bedType;
    }

    public void setBedType(BedType bedType) {
        this.bedType = bedType;
    }

    public BedStatus getBedStatue() {
        return bedStatue;
    }

    public void setBedStatue(BedStatus bedStatue) {
        this.bedStatue = bedStatue;
    }

    public String getBedDescription() {
        return bedDescription;
    }

    public void setBedDescription(String bedDescription) {
        this.bedDescription = bedDescription;
    }

    public BedPhysicalCondition getPhysicalState() {
        return physicalState;
    }

    public void setPhysicalState(BedPhysicalCondition physicalState) {
        this.physicalState = physicalState;
    }

    public Timestamp getBedPurchaseDate() {
        return bedPurchaseDate;
    }

    public void setBedPurchaseDate(Timestamp bedPurchaseDate) {
        this.bedPurchaseDate = bedPurchaseDate;
    }

    public BedCleaningStatus getBedCleaningStatus() {
        return bedCleaningStatus;
    }

    public void setBedCleaningStatus(BedCleaningStatus bedCleaningStatus) {
        this.bedCleaningStatus = bedCleaningStatus;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getRemainingLifeSpan() {
        if (this.bedPurchaseDate != null && this.expirationDate != null) {
            long diffInMillis = this.expirationDate.getTime() - this.bedPurchaseDate.getTime();
            long diffInDays = diffInMillis / (1000 * 60 * 60 * 24); // Convert milliseconds to days
            return Math.toIntExact(diffInDays); // Convert long to int
        } else {
            return null; // or any default value as per your requirement
        }
    }

    public void setRemainingLifeSpan(Integer remainingLifeSpan) {
        RemainingLifeSpan = remainingLifeSpan;
    }

    public Integer getPoids() {
        return poids;
    }

    public void setPoids(Integer poids) {
        this.poids = poids;
    }

    public Room getRoomBed() {
        return roomBed;
    }

    public void setRoomBed(Room roomBed) {
        this.roomBed = roomBed;
    }

    public Set<BedEquipLink> getBedEquipLinkSet() {
        return bedEquipLinkSet;
    }

    public void setBedEquipLinkSet(Set<BedEquipLink> bedEquipLinkSet) {
        this.bedEquipLinkSet = bedEquipLinkSet;
    }


    public List<Long> getDeletedEquipmentIds() {
        return deletedEquipmentIds;
    }

    public void setDeletedEquipmentIds(List<Long> deletedEquipmentIds) {
        this.deletedEquipmentIds = deletedEquipmentIds;
    }

}
