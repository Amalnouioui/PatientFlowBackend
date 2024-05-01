package com.core.patient.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name="History")
public class Admission_History {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "History_Key", nullable = false, unique = true)
    private Integer AdmissionHistoryKey;
    @Column(name = "History_OccupantKy", nullable = false)
    private Integer HistoryOccupantKy;



    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "History_PlannedUnxTmBgn" )
    private Date history_PlannedUnxTmBgn;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "History_PlannedUnxTmEnd")
    private Date history_PlannedUnxTmEnd;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "History_RealUnxTmBgn")
    private Date bedLocked_RealUnxTmBgn;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "History_RealUnxTmEnd")
    private Date bedLocked_RealUnxTmEnd;

    @ManyToOne
    @JoinColumn(name = "patient_ref", referencedColumnName = "Patient_Key")
    @JsonIdentityReference(alwaysAsId = true)
    private  Patient patient;

}
