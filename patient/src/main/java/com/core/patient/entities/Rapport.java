package com.core.patient.entities;

import com.core.patient.entities.enumeration.BloodPressure;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Rapport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rapport_Key", nullable = false, unique = true)
    private int rapportKey;


    @Column(name = "Rapport_Date")
    private Date rapportDate;




    @Column(name="Disease", nullable=false)
    private String disease;

    @Column(name="Fever", nullable=false)
    private boolean fever;

    @Column(name="Cough", nullable=false)
    private boolean Cough;

    @Column(name="Fatigue", nullable=false)
    private boolean fatigue;

    @Column(name="bloodPressure", nullable=false)
    private BloodPressure bloodPressure;

    @Column(name="Cholesterol", nullable=false)
    private BloodPressure cholesterol;
    @ManyToOne
    @JoinColumn(name = "Doctor_ref")

    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "Patient_ref")

    private Patient patient;

    public int getRapportKey() {
        return rapportKey;
    }

    public void setRapportKey(int rapportKey) {
        this.rapportKey = rapportKey;
    }

    public Date getRapportDate() {
        return rapportDate;
    }

    public void setRapportDate(Date rapportDate) {
        this.rapportDate = rapportDate;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public boolean isFever() {
        return fever;
    }

    public void setFever(boolean fever) {
        this.fever = fever;
    }

    public boolean isCough() {
        return Cough;
    }

    public void setCough(boolean cough) {
        Cough = cough;
    }

    public boolean isFatigue() {
        return fatigue;
    }

    public void setFatigue(boolean fatigue) {
        this.fatigue = fatigue;
    }

    public BloodPressure getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(BloodPressure bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public BloodPressure getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(BloodPressure cholesterol) {
        this.cholesterol = cholesterol;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
