package com.pfa.backend.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class FormeJuridique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @OneToMany(mappedBy = "formeJuridique")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Entreprise> entreprises = new ArrayList<>();
}