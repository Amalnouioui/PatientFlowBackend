package com.core.Parameterization.Entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Data
@Table(name="_User")
public class User   {

    @Id
    @Column(name="User_ky", nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(name="User_FirstName", nullable=false)
    private String FirstName;
    @Column(name="User_LastName", nullable=false)
    private String LastName;
    @Column(name="Username", nullable=false)
    private String Username ;
    @Column(name="password", nullable=false)
    private String password ;
}
