package com.core.Parameterization.Entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "serviceKy")

@Table(name = "Service")

@Entity
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Service_Ky",nullable = false)
    private Long serviceKy;
    @Column(name="Service_Name",nullable = false)
    private String serviceName;
    
    @OneToMany(mappedBy = "service",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Room> rooms;

    @JsonIgnore
    @OneToMany(mappedBy = "service",cascade = CascadeType.ALL,orphanRemoval = true)
    Set<CareUnitServiceLink>careUnitServiceLinkSet;
    
    
    private boolean isSurgeryService; // Champ indiquant si c'est un service de chirurgie ou non


    public Long getServiceKy() {
        return serviceKy;
    }

    public void setServiceKy(Long iServiceKy) {
        this.serviceKy = iServiceKy;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String iServiceName) {
        serviceName = iServiceName;
    }

    public Set<CareUnitServiceLink> getCareUnitServiceLinkSet() {
        return careUnitServiceLinkSet;
    }

    public void setCareUnitServiceLinkSet(Set<CareUnitServiceLink> iCareUnitServiceLinkSet) {
        this.careUnitServiceLinkSet = iCareUnitServiceLinkSet;
    }

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public boolean isSurgeryService() {
		return isSurgeryService;
	}

	public void setSurgeryService(boolean isSurgeryService) {
		this.isSurgeryService = isSurgeryService;
	}

}
