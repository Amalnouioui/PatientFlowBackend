package com.core.patient.entities;

import com.core.patient.entities.enumeration.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Doctor")
public class Doctor {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Patient_Key", nullable = false, unique = true)
    private int doctorKey;

    @Column(name = "Patient_FirstName", nullable = false)
    private String doctorFirtName;

    @Column(name = "Patient_LastName", nullable = false)
    private String doctorLastName;

    @Column(name = "Patient_BirthDate")
    private Date patientBirthDate;



    @Column(name = "Patient_IdentityNumber",unique = true)
    private String doctorIdentity;

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


    @Enumerated(EnumType.STRING)
    @Column(name = "country")
    private Country country;




    @OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL)
    Set<Rapport> rapportSet;

    public int getDoctorKey() {
        return doctorKey;
    }

    public void setDoctorKey(int doctorKey) {
        this.doctorKey = doctorKey;
    }

    public String getDoctorFirtName() {
        return doctorFirtName;
    }

    public void setDoctorFirtName(String doctorFirtName) {
        this.doctorFirtName = doctorFirtName;
    }

    public String getDoctorLastName() {
        return doctorLastName;
    }

    public void setDoctorLastName(String doctorLastName) {
        this.doctorLastName = doctorLastName;
    }

    public Date getPatientBirthDate() {
        return patientBirthDate;
    }

    public void setPatientBirthDate(Date patientBirthDate) {
        this.patientBirthDate = patientBirthDate;
    }

    public String getDoctorIdentity() {
        return doctorIdentity;
    }

    public void setDoctorIdentity(String doctorIdentity) {
        this.doctorIdentity = doctorIdentity;
    }

    public IdentityType getIdentity() {
        return identity;
    }

    public void setIdentity(IdentityType identity) {
        this.identity = identity;
    }

    public Nationality getPatientNationality() {
        return patientNationality;
    }

    public void setPatientNationality(Nationality patientNationality) {
        this.patientNationality = patientNationality;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<Rapport> getRapportSet() {
        return rapportSet;
    }

    public void setRapportSet(Set<Rapport> rapportSet) {
        this.rapportSet = rapportSet;
    }
}
