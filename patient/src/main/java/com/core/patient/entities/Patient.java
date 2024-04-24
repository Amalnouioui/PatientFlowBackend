package com.core.patient.entities;

import java.util.Date;
import java.util.List;


import com.core.patient.entities.enumeration.Country;
import com.core.patient.entities.enumeration.Gender;
import com.core.patient.entities.enumeration.IdentityType;
import com.core.patient.entities.enumeration.Nationality;
import com.core.patient.entities.enumeration.SocialStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Patient")
public class Patient {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Patient_Key", nullable = false, unique = true)
    private int patientKey;
	
	@Column(name = "Patient_FirstName", nullable = false)
    private String patientFirstName;
	
	@Column(name = "Patient_LastName", nullable = false)
    private String patientLastName;
	
	@Column(name = "Patient_BirthDate")
    private Date patientBirthDate;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "Patient_Gender")
    private Gender patientGender;
	
	@Column(name = "Patient_IdentityNumber",unique = true)
    private String patientIdentityNumber;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "identity")
    private IdentityType identity;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "Patient_Nationality")
    private Nationality patientNationality;
    
    @Column(name = "adress")
    private String adress;
    
    @Column(name = "email",unique = true)
    private String email;
    
    @Column(name = "phone",unique = true)
    private String phone;
    
    @Column(name = "patientSize")
    private double patientSize;
    
    @Column(name = "patientWeight")
    private double patientWeight;
    
    @Column(name = "patientRemarks")
    private String patientRemarks;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "country")
    private Country country;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "socialStatus")
    private SocialStatus socialStatus;




    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Historique> historiques;
    
    
    
    
    
    

    
	

}
