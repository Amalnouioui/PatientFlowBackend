package com.core.patient.entities;

import com.core.patient.entities.enumeration.*;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Historique")

public class Historique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "historique_key", nullable = false, unique = true)
    private Integer historiquekey;



    @Column(name = "Patient_placementDate")
    private Date placementDate;



    @Column(name = "old_Careunit")
    private String OldCareUnit;

    @Column(name = "New_CareUnit")
    private String NewCareUnit;


    @Column(name = "Old_Bed")
    private Integer oldBed;

    @Column(name = "New_Bed")
    private Integer newBed;

    @Column(name = "Old_Room")
    private Integer oldRoom;

    @Column(name = "New_Room")
    private Integer newRoom;

@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "patient_ref", referencedColumnName = "Patient_Key")
   // @JsonIdentityReference(alwaysAsId = true)
    private  Patient patient;

    public Integer getHistoriquekey() {
        return historiquekey;
    }

    public void setHistoriquekey(Integer historiquekey) {
        this.historiquekey = historiquekey;
    }

    public Date getPlacementDate() {
        return placementDate;
    }

    public void setPlacementDate(Date placementDate) {
        this.placementDate = placementDate;
    }

    public String getOldCareUnit() {
        return OldCareUnit;
    }

    public void setOldCareUnit(String oldCareUnit) {
        OldCareUnit = oldCareUnit;
    }

    public String getNewCareUnit() {
        return NewCareUnit;
    }

    public void setNewCareUnit(String newCareUnit) {
        NewCareUnit = newCareUnit;
    }


    public Integer getOldBed() {
        return oldBed;
    }

    public void setOldBed(Integer oldBed) {
        this.oldBed = oldBed;
    }

    public Integer getNewBed() {
        return newBed;
    }

    public void setNewBed(Integer newBed) {
        this.newBed = newBed;
    }

    public Integer getOldRoom() {
        return oldRoom;
    }

    public void setOldRoom(Integer oldRoom) {
        this.oldRoom = oldRoom;
    }

    public Integer getNewRoom() {
        return newRoom;
    }

    public void setNewRoom(Integer newRoom) {
        this.newRoom = newRoom;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
