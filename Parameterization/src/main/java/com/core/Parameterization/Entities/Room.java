package com.core.Parameterization.Entities;
import com.core.Parameterization.Entities.Enumeration.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "roomKey")
@Table(name = "Room")

public class Room {
    @Id
    @Column(name="Room_Key", nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomKey;
    @Column(name="Room_Number", nullable=false)
    private Integer roomName ; //String
    @Enumerated(EnumType.STRING)
    @Column(name="Room_Type", nullable=false)
    private RoomType roomType ;

    @Enumerated(EnumType.STRING)
    @Column(name="Room_status",nullable = false)
    private RoomStatus roomStatue;

    @Column(name="Room_Capacity",nullable = false)
    private int roomCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name="cleaning_Status",nullable = false)
    private CleaningState cleaningState;
    @Column(name="Room_Responsible")
    private String roomResponsible;

    public String getRoomResponsible() {
        return roomResponsible;
    }

    public void setRoomResponsible(String roomResponsible) {
        this.roomResponsible = roomResponsible;
    }
    
    @ManyToOne
    @JoinColumn(name = "service_ky", referencedColumnName = "service_Ky")
    @JsonIdentityReference(alwaysAsId = true)
    private ServiceEntity service;

   @ManyToOne
   @JoinColumn(name = "CareUnit_Ref", referencedColumnName = "CareUnit_key")
   @JsonIdentityReference(alwaysAsId = true)
   private  CareUnit careunitRoom;


    @OneToMany(mappedBy = "roomBed",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Bed>roomBed;


    public Integer getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(Integer roomKey) {
        this.roomKey = roomKey;
    }

    public Integer getRoomName() {
        return roomName;
    }

    public void setRoomName(Integer roomName) {
        this.roomName = roomName;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public RoomStatus getRoomStatue() {
        return roomStatue;
    }

    public void setRoomStatue(RoomStatus roomStatue) {
        this.roomStatue = roomStatue;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public CleaningState getCleaningState() {
        return cleaningState;
    }

    public void setCleaningState(CleaningState cleaningState) {
        this.cleaningState = cleaningState;
    }

    public CareUnit getCareunitRoom() {
        return careunitRoom;
    }

    public void setCareunitRoom(CareUnit careunitRoom) {
        this.careunitRoom = careunitRoom;
    }

    public List<Bed> getRoomBed() {
        return roomBed;
    }

    public void setRoomBed(List<Bed> roomBed) {
        this.roomBed = roomBed;
    }

	public ServiceEntity getService() {
		return service;
	}

	public void setService(ServiceEntity service) {
		this.service = service;
	}


}
