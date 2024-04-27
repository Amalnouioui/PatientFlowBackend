package com.core.patient.entities;

import com.core.patient.entities.enumeration.Gender;
import com.core.patient.entities.enumeration.IdentityType;
import com.core.patient.entities.enumeration.Nationality;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_Key", nullable = false, unique = true)
    private Integer doctorKey;

    @Column(name = "doctor_FirstName", nullable = false)
    private String doctorFirstName;

    @Column(name = "doctor_LastName", nullable = false)
    private String doctorLastName;

    @Column(name = "doctor_BirthDate")
    private Date doctorBirthDate;



    @Column(name = "doctor_IdentityNumber",unique = true)
    private String doctorIdentityNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "identity")
    private IdentityType identity;

    @Enumerated(EnumType.STRING)
    @Column(name = "Patient_Nationality")
    private Nationality patientNationality;

    @Column(name = "adress")
    private String adress;
    @Column(name = "Email")
    private String email;
    // @JsonIgnore
    @Getter
    @OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Rapport> rapportset;


}
