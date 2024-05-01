package com.core.Parameterization.Entities;


import com.core.Parameterization.Entities.Enumeration.OccupantType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BedLocked {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BedLocked_Ky", nullable = false, unique = true)
    private Integer bedLockedKy;
    @Column(name = "BedLocked_OccupantKy", nullable = false)
    private Integer bedLockedOccupantKy;

    @Enumerated(EnumType.STRING)
    @Column(name = "BedLocked_OccupantType",nullable=false)
    private OccupantType bedLockedOccupantType;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "BedLocked_PlannedUnxTmBgn" )
    private Date bedLocked_PlannedUnxTmBgn;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "BedLocked_PlannedUnxTmEnd")
    private Date bedLocked_PlannedUnxTmEnd;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "BedLocked_RealUnxTmBgn")
    private Date bedLocked_RealUnxTmBgn;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "BedLocked_RealUnxTmEnd")
    private Date bedLocked_RealUnxTmEnd;



    public Bed getBed() {
        return bed;
    }

    public void setBed(Bed bed) {
        this.bed = bed;
    }

    @ManyToOne
    @JoinColumn(name="bed_ref", referencedColumnName = "Bed_Key")
    @JsonIdentityReference(alwaysAsId = true)
    private Bed bed;

    public Integer getBedLockedKy() {
        return bedLockedKy;
    }

    public void setBedLockedKy(Integer bedLockedKy) {
        this.bedLockedKy = bedLockedKy;
    }

    public Integer getBedLockedOccupantKy() {
        return bedLockedOccupantKy;
    }

    public void setBedLockedOccupantKy(Integer bedLockedOccupantKy) {
        this.bedLockedOccupantKy = bedLockedOccupantKy;
    }

    public OccupantType getBedLockedOccupantType() {
        return bedLockedOccupantType;
    }

    public void setBedLockedOccupantType(OccupantType bedLockedOccupantType) {
        this.bedLockedOccupantType = bedLockedOccupantType;
    }


    public Date getBedLocked_PlannedUnxTmBgn() {
        return bedLocked_PlannedUnxTmBgn;
    }

    public void setBedLocked_PlannedUnxTmBgn(Date bedLocked_PlannedUnxTmBgn) {
        this.bedLocked_PlannedUnxTmBgn = bedLocked_PlannedUnxTmBgn;
    }

    public Date getBedLocked_PlannedUnxTmEnd() {
        return bedLocked_PlannedUnxTmEnd;
    }

    public void setBedLocked_PlannedUnxTmEnd(Date bedLocked_PlannedUnxTmEnd) {
        this.bedLocked_PlannedUnxTmEnd = bedLocked_PlannedUnxTmEnd;
    }

    public Date getBedLocked_RealUnxTmBgn() {
        return bedLocked_RealUnxTmBgn;
    }

    public void setBedLocked_RealUnxTmBgn(Date bedLocked_RealUnxTmBgn) {
        this.bedLocked_RealUnxTmBgn = bedLocked_RealUnxTmBgn;
    }

    public Date getBedLocked_RealUnxTmEnd() {
        return bedLocked_RealUnxTmEnd;
    }

    public void setBedLocked_RealUnxTmEnd(Date bedLocked_RealUnxTmEnd) {
        this.bedLocked_RealUnxTmEnd = bedLocked_RealUnxTmEnd;
    }

    @Override
    public String toString() {
        return "BedLocked{" +
                "bedLockedKy=" + bedLockedKy +
                ", bedLockedOccupantKy=" + bedLockedOccupantKy +
                ", bedLockedOccupantType=" + bedLockedOccupantType +
                ", bedLocked_PlannedUnxTmBgn=" + bedLocked_PlannedUnxTmBgn +
                ", bedLocked_PlannedUnxTmEnd=" + bedLocked_PlannedUnxTmEnd +
                ", bedLocked_RealUnxTmBgn=" + bedLocked_RealUnxTmBgn +
                ", bedLocked_RealUnxTmEnd=" + bedLocked_RealUnxTmEnd +
                ", bed=" + bed +
                '}';
    }
}
