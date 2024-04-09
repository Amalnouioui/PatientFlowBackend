package com.core.Parameterization.Entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "CareUnitServiceLink")
public class CareUnitServiceLink {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "careUnitServiceLinkKy") // Nom de la colonne pour l'identifiant
    private Long careUnitServiceLinkKy;

	@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "CareUnitServiceLink_PrntCareUnitKy")

    private CareUnit careUnit;

    @ManyToOne
    @JoinColumn(name = "CareUnitServiceLink_PrntServiceKy")

    private ServiceEntity service;


}
