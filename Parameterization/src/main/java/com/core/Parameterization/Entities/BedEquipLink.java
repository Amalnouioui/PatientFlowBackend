package com.core.Parameterization.Entities;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BedEquipLink")
public class BedEquipLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long BedEquipLink_Ky;

    @ManyToOne
    @JoinColumn(name = "BedEquipLink_PrntBedKy")
    private Bed bed;

    @ManyToOne
    @JoinColumn(name = "BedEquipLink_PrntEquipmentKy")
    private Equipment equipment;


}
