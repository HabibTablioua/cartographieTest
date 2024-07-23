package com.pfa.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfa.backend.DTO.EntrepriseDTO;
import com.pfa.backend.DTO.HistoriqueDentrepriseDTO;
import com.pfa.backend.entity.*;
import com.pfa.backend.repository.EntrepriseRepository;
import com.pfa.backend.repository.FormeJuridiqueRepository;
import com.pfa.backend.repository.SecteurDactiviteRepository;
import com.pfa.backend.service.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/entreprises")
public class EntrepriseController {

    @Autowired
    private EntrepriseService entrepriseService;

    @Autowired
    private HistoriqueDentrepriseService historiqueDentrepriseService;

    @Autowired
    private TelephoneService telephoneService;

    @Autowired
    private FaxService faxService;

    @Autowired
    private GerantService gerantService;
    @Autowired
    private FormeJuridiqueRepository formeJuridiqueRepository;
    @Autowired
    private SecteurDactiviteRepository secteurDactiviteRepository;
    @Autowired
    private EntrepriseRepository entrepriseRepository;
    @Autowired
    private GerantService GerantService;

    @PutMapping("/gerants/{entrepriseId}")
    public ResponseEntity<List<Gerant>> updateGerantsByEntrepriseId(
            @PathVariable Long entrepriseId,
            @RequestBody List<Gerant> gerants) {
        List<Gerant> updatedGerants = gerantService.updateGerantsByEntrepriseId(entrepriseId, gerants);
        return ResponseEntity.ok(updatedGerants);
    }
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Entreprise> addEntreprise(
            @RequestParam(value = "denomination", required = false) String denomination,
            @RequestParam(value = "capitalSocial", required = false) Float capitalSocial,
            @RequestParam(value = "ice", required = false) Integer ice,
            @RequestParam(value = "identifiantFiscal", required = false) Integer identifiantFiscal,
            @RequestParam(value = "numRegistreCommerce", required = false) Integer numRegistreCommerce,
            @RequestParam(value = "numPatente", required = false) Integer numPatente,
            @RequestParam(value = "numAffiliationCnss", required = false) Integer numAffiliationCnss,
            @RequestParam(value = "adresse", required = false) String adresse,
            @RequestParam(value = "ville", required = false) String ville,
            @RequestParam(value = "telephones", required = false) List<String> telephones,
            @RequestParam(value = "faxes", required = false) List<String> faxes,
            @RequestParam(value = "gerants", required = false) String gerants,
            @RequestParam(value = "mail", required = false) String mail,
            @RequestParam(value = "siteWeb", required = false) String siteWeb,
            @RequestParam(value = "nombreEmployes", required = false) Integer nombreEmployes,
            @RequestParam(value = "latitude", required = false) String latitude,
            @RequestParam(value = "longitude", required = false) String longitude,
            @RequestParam(value = "dateCreation", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateCreation,
            @RequestParam(value = "dateCessationActivite", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateCessationActivite,
            @RequestParam(value = "secteurId", required = false) Long secteurId,
            @RequestParam(value = "formeJuridiqueId", required = false) Long formeJuridiqueId,
            @RequestParam(value = "logo", required = false) MultipartFile logo
    ) {
        Entreprise entreprise = new Entreprise();
        entreprise.setDenomination(denomination);
        entreprise.setCapitalSocial(capitalSocial);
        entreprise.setIce(ice);
        entreprise.setIdentifiantFiscal(identifiantFiscal);
        entreprise.setNumRegistreCommerce(numRegistreCommerce);
        entreprise.setNumPatente(numPatente);
        entreprise.setNumAffiliationCnss(numAffiliationCnss);
        entreprise.setAdresse(adresse);
        entreprise.setVille(ville);
        entreprise.setMail(mail);
        entreprise.setSiteWeb(siteWeb);
        entreprise.setNombreEmployes(nombreEmployes);
        entreprise.setLatitude(latitude);
        entreprise.setLongitude(longitude);
        entreprise.setDateCreation(dateCreation);
        entreprise.setDateCessationActivite(dateCessationActivite);

        if (secteurId != null) {
            SecteurDactivite secteur = new SecteurDactivite();
            secteur.setId(secteurId);
            entreprise.setSecteurDactivite(secteur);
        }

        if (formeJuridiqueId != null) {
            FormeJuridique forme = new FormeJuridique();
            forme.setId(formeJuridiqueId);
            entreprise.setFormeJuridique(forme);
        }

        entreprise.addTelephones(telephones);
        entreprise.addFaxes(faxes);
        if (gerants != null && !gerants.isEmpty()) {
            entreprise.addGerants(gerants);
        }
        if (logo != null && !logo.isEmpty()) {
            try {
                entreprise.setLogo(logo.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Entreprise savedEntreprise = entrepriseService.createEntreprise(entreprise);
        return ResponseEntity.ok(savedEntreprise);
    }






    @GetMapping("/villes")
    public List<String> getAllVilles() {
        return entrepriseService.getAllVilles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntrepriseDTO> getEntrepriseById(@PathVariable Long id) {
        Optional<EntrepriseDTO> entrepriseOpt = entrepriseService.getEntrepriseById(id);
        if (entrepriseOpt.isPresent()) {
            return ResponseEntity.ok(entrepriseOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}/telephones")
    public ResponseEntity<List<Telephone>> getTelephonesByEntrepriseId(@PathVariable Long id) {
        List<Telephone> telephones = telephoneService.getTelephonesByEntrepriseId(id);
        if (telephones.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(telephones);
        }
    }
    @GetMapping("/{id}/faxes")
    public ResponseEntity<List<Fax>> getFaxesByEntrepriseId(@PathVariable Long id) {
        List<Fax> faxes = faxService.getFaxesByEntrepriseId(id);
        if (faxes.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(faxes);
        }
    }
    @GetMapping("/{id}/gerants")
    public ResponseEntity<List<Gerant>> getGerantsByEntrepriseId(@PathVariable Long id) {
        List<Gerant> gerants = gerantService.getGerantsByEntrepriseId(id);
        if (gerants.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(gerants);
        }
    }
    @GetMapping
    public ResponseEntity<List<EntrepriseDTO>> getAllEntreprises() {
        List<EntrepriseDTO> entreprises = entrepriseService.getAllEntreprises();
        return ResponseEntity.ok(entreprises);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntrepriseDTO> updateEntreprise(@PathVariable Long id, @RequestParam Map<String, Object> updates, @RequestParam(value = "logo", required = false) MultipartFile logo) {
        try {
            EntrepriseDTO entrepriseMiseAJour = entrepriseService.updateEntreprise(id, updates, logo);
            return ResponseEntity.ok(entrepriseMiseAJour);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntreprise(@PathVariable Long id) {
        entrepriseService.deleteEntreprise(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/historiques")
    public ResponseEntity<List<HistoriqueDentrepriseDTO>> getHistoriqueByEntrepriseId(@PathVariable Long id) {
        List<HistoriqueDentrepriseDTO> historiqueList = historiqueDentrepriseService.getHistoriqueByEntrepriseId(id);
        return ResponseEntity.ok(historiqueList);
    }
    @GetMapping("/filter")
    public ResponseEntity<List<EntrepriseDTO>> filterEntreprises(
            @RequestParam(value = "ville", required = false) String ville,
            @RequestParam(value = "denomination", required = false) String denomination,
            @RequestParam(value = "secteurNom", required = false) String secteurNom,
            @RequestParam(value = "formeJuridiqueNom", required = false) String formeJuridiqueNom
    ) {
        List<EntrepriseDTO> entreprises = entrepriseService.filterEntreprises(ville, denomination, secteurNom, formeJuridiqueNom);

        // Retourner une réponse appropriée si la liste est vide
        if (entreprises.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(entreprises);
    }
}