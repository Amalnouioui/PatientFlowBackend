package com.core.Parameterization.Entities;

import com.core.Parameterization.Entities.Enumeration.CompanionRelation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="RoomCompanion")
public class RoomCompanion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoomCompanion_Ky", nullable = false, unique = true)
    private Integer roomCompanionKy;

    @Column(name = "RoomCompanion_Name", nullable = false)
    private String roomCompanionName;

    @Column(name = "RoomCompanion_FirstName", nullable = false)
    private String roomCompanionFirstName;

    @Column(name = "RoomCompanion_BirthDate")
    private Date roomCompanionBirthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "RoomCompanion_Relation")
    private CompanionRelation roomCompanionRelation;

    @Column(name = "RoomCompanion_Remarque")
    private String roomCompanion_Remarque;

    public int getRoomCompanionKy() {
        return roomCompanionKy;
    }

    public void setRoomCompanionKy(int roomCompanionKy) {
        this.roomCompanionKy = roomCompanionKy;
    }

    public String getRoomCompanionName() {
        return roomCompanionName;
    }

    public void setRoomCompanionName(String roomCompanionName) {
        this.roomCompanionName = roomCompanionName;
    }

    public String getRoomCompanionFirstName() {
        return roomCompanionFirstName;
    }

    public void setRoomCompanionFirstName(String roomCompanionFirstName) {
        this.roomCompanionFirstName = roomCompanionFirstName;
    }

    public Date getRoomCompanionBirthDate() {
        return roomCompanionBirthDate;
    }

    public void setRoomCompanionBirthDate(Date roomCompanionBirthDate) {
        this.roomCompanionBirthDate = roomCompanionBirthDate;
    }

    public CompanionRelation getRoomCompanionRelation() {
        return roomCompanionRelation;
    }

    public void setRoomCompanionRelation(CompanionRelation roomCompanionRelation) {
        this.roomCompanionRelation = roomCompanionRelation;
    }

    public String getRoomCompanion_Remarque() {
        return roomCompanion_Remarque;
    }

    public void setRoomCompanion_Remarque(String roomCompanion_Remarque) {
        this.roomCompanion_Remarque = roomCompanion_Remarque;
    }
}