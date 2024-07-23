package com.pfa.backend.DTO;


import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class EntrepriseDTO {
    private Long id;
    private String denomination;
    private Float capitalSocial;
    private Integer ice;
    private Integer identifiantFiscal;
    private Integer numRegistreCommerce;
    private Integer numPatente;
    private Integer numAffiliationCnss;
    private String adresse;
    private String ville;
    private String mail;
    private String siteWeb;
    private Integer nombreEmployes;
    private String latitude;
    private String longitude;
    private Date dateCreation;
    private String logo;
    private Date dateCessationActivite;
    private SecteurDactiviteDTO secteurDactivite; // Modifié pour être un objet
    private FormeJuridiqueDTO formeJuridique;  // Modifié pour être un objet
    private List<TelephoneDTO> telephones = new ArrayList<>();
    private List<FaxDTO> faxes = new ArrayList<>();
    private List<GerantDTO> gerants = new ArrayList<>();
    private List<HistoriqueDentrepriseDTO> historiqueDentreprise;
}