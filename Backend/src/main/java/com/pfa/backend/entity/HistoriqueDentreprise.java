package com.pfa.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class HistoriqueDentreprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;

    private String attributModifie; // Nom de l'attribut modifié
    private String ancienneValeur;    // Valeur avant modification
    private String nouvelleValeur;    // Valeur après modification
    private Date dateModification;  // Date de la modification
}


