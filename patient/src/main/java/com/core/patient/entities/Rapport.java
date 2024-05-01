package com.core.patient.entities;

import com.core.patient.entities.enumeration.BloodPressure;
import com.core.patient.entities.enumeration.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Rapport")
@Getter
@Setter
public class Rapport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rapport_Key", nullable = false, unique = true)
    private int rapportKey;

    @Column(name = "disease", nullable = false)
    private String disease;

    @Column(name = "fever", nullable = false)
    private boolean fever;
    @Column(name = "Cough", nullable = false)
    private boolean Cough;
    @Column(name = "fatigue", nullable = false)
    private boolean fatigue;


    @Column(name = "difficulty Breathing", nullable = false)
    private boolean     difficulty_Breathing;

    @Column(name = "rapportDate")
    private Date rapportDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "bloodPressure")
    private BloodPressure bloodPressure;
    @Enumerated(EnumType.STRING)
    @Column(name = "cholesterol")
    private BloodPressure cholesterol;
    @Column(name = "remarque", nullable = false)
    private String remarque;


@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "patient_ref")
    private Patient patient ;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "doctor_ref")
    private Doctor doctor ;

}
