package com.core.patient.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Consultation")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultationKey", nullable = false, unique = true)
    private Integer consultationKey ;

    @Column(name = "consultationDate", nullable = false)
    private String consultationDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "patient_ref")
    private Patient patient ;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "doctor_ref")
    private Doctor doctor ;

}
