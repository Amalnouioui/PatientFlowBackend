package com.core.Parameterization.Entities;

import com.core.Parameterization.Entities.Enumeration.ServiceType;
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

    @Enumerated(EnumType.STRING)
    @Column(name="Service_serviceType",nullable = false)

    private ServiceType serviceType;

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public List<CareUnit> getServiceCareunits() {
        return serviceCareunits;
    }

    public void setServiceCareunits(List<CareUnit> serviceCareunits) {
        this.serviceCareunits = serviceCareunits;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "serviceCareunits",cascade = CascadeType.ALL,orphanRemoval = true)

    List<CareUnit>serviceCareunits;
    


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










}
