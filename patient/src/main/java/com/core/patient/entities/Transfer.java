package com.core.patient.entities;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="deplacmenet")

public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Transfer_Ky", nullable = false, unique = true)
    private Integer transferKy;



    @Column(name = "Transfer_DateTime")
    private Date placementDate;



    @Column(name = "old_Careunit")
    private String oldCareUnit;

    @Column(name = "New_CareUnit")
    private String newCareUnit;


    @Column(name = "Old_Bed")
    private Integer Transfer_OldBedKy;

    @Column(name = "New_Bed")
    private Integer Transfer_NewBedKy;

    @Column(name = "Old_Room")
    private Integer oldRoom;

    @Column(name = "New_Room")
    private Integer newRoom;


    @ManyToOne
    @JoinColumn(name = "Transfer_PatientKy", referencedColumnName = "Patient_Key")
    @JsonIdentityReference(alwaysAsId = true)
    private  Patient patient;


    @ManyToOne
    @JoinColumn(name = "Transfer_PatientKy", referencedColumnName = "Bed_Key")
    @JsonIdentityReference(alwaysAsId = true)
    private  Patient patient;
    public Integer getDeplacmentKy() {
        return deplacmentKy;
    }

    public void setDeplacmentKy(Integer deplacmentKy) {
        this.deplacmentKy = deplacmentKy;
    }

    public Date getPlacementDate() {
        return placementDate;
    }

    public void setPlacementDate(Date placementDate) {
        this.placementDate = placementDate;
    }

    public String getOldCareUnit() {
        return oldCareUnit;
    }

    public void setOldCareUnit(String oldCareUnit) {
        this.oldCareUnit = oldCareUnit;
    }

    public String getNewCareUnit() {
        return newCareUnit;
    }

    public void setNewCareUnit(String newCareUnit) {
        this.newCareUnit = newCareUnit;
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setNewRoom(Integer newRoom) {
        this.newRoom = newRoom;
    }


}
