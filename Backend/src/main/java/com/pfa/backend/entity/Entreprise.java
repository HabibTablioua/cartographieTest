package com.pfa.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.util.*;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(EntrepriseListener.class)
public class Entreprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
    private List<Telephone> telephones = new ArrayList<>();

    @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
    private List<Fax> faxes = new ArrayList<>();

    @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
    private List<Gerant> gerants = new ArrayList<>();
    public void addTelephones(List<String> telephones) {
        if (telephones != null) {
            for (String numero : telephones) {
                Telephone telephone = new Telephone();
                telephone.setNumero(numero);
                telephone.setEntreprise(this);
                this.telephones.add(telephone);
            }
        }
    }

    public void addFaxes(List<String> faxes) {
        if (faxes != null) {
            for (String numero : faxes) {
                Fax fax = new Fax();
                fax.setNumero(numero);
                fax.setEntreprise(this);
                this.faxes.add(fax);
            }
        }
    }

    public void addGerants(String gerants) {
        if (gerants != null && !gerants.isEmpty()) {
            String[] gerantsArray = gerants.split(",");
            for (String fullName : gerantsArray) {
                String[] nameParts = fullName.trim().split(" ");
                if (nameParts.length == 2) {
                    Gerant gerant = new Gerant();
                    gerant.setNom(nameParts[0].trim());
                    gerant.setPrenom(nameParts[1].trim());
                    gerant.setEntreprise(this);
                    this.gerants.add(gerant);
                } else {
                    System.out.println("Nom complet invalide pour le gérant : " + fullName);
                }
            }
        }
    }

    public void updateTelephone(String oldNumber, String newNumber) {
        for (Telephone telephone : telephones) {
            if (telephone.getNumero().equals(oldNumber)) {
                telephone.setNumero(newNumber);
                break;
            }
        }
    }

    public void updateFax(String oldNumber, String newNumber) {
        for (Fax fax : faxes) {
            if (fax.getNumero().equals(oldNumber)) {
                fax.setNumero(newNumber);
                break;
            }
        }
    }

    public void updateGerant(Long gerantId, String newNom, String newPrenom) {
        for (Gerant gerant : gerants) {
            if (gerant.getId().equals(gerantId)) {
                gerant.setNom(newNom);
                gerant.setPrenom(newPrenom);
                break;
            }
        }
    }

    private String mail;
    private String siteWeb;
    private Integer nombreEmployes;
    private String latitude;
    private String longitude;
    private Date dateCreation;
    @Lob
    @Column(name = "logo", columnDefinition="LONGBLOB")
    private byte[] logo;
    private Date dateCessationActivite;

    @ManyToOne
    @JoinColumn(name = "secteur_id")
    private SecteurDactivite secteurDactivite;

    @ManyToOne
    @JoinColumn(name = "forme_juridique_id")
    private FormeJuridique formeJuridique;

    @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
    private List<HistoriqueDentreprise> historiqueDentreprise = new ArrayList<>();

    @Transient
    private Map<String, Object> originalValues = new HashMap<>();

    @PostLoad
    public void storeOriginalValues() {
        BeanWrapper beanWrapper = new BeanWrapperImpl(this);
        for (Field field : getClass().getDeclaredFields()) {
            originalValues.put(field.getName(), beanWrapper.getPropertyValue(field.getName()));
        }
    }
}