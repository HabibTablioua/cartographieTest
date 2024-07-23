package com.pfa.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Fax {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;

    @ManyToOne
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;
}
