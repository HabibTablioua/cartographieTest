package com.pfa.backend.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class HistoriqueDentrepriseDTO {
    private Long id;
    private String attributModifie;
    private String ancienneValeur;
    private String nouvelleValeur;
    private Date dateModification;
}