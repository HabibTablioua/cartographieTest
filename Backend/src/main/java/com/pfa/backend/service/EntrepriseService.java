package com.pfa.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfa.backend.DTO.*;
import com.pfa.backend.entity.*;
import com.pfa.backend.repository.EntrepriseRepository;
import com.pfa.backend.repository.FormeJuridiqueRepository;
import com.pfa.backend.repository.HistoriqueDentrepriseRepository;
import com.pfa.backend.repository.SecteurDactiviteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import static org.apache.tomcat.util.http.FastHttpDateFormat.parseDate;

@Service
public class EntrepriseService {

    @Autowired
    private EntrepriseRepository entrepriseRepository;
    @Autowired
    private HistoriqueDentrepriseRepository historiqueDentrepriseRepository;
    @Autowired
    private GerantService gerantService;
    @Autowired
    private TelephoneService telephoneService;
    @Autowired
    private  FaxService faxService;
    @Autowired
    private SecteurDactiviteRepository secteurDactiviteRepository;

    @Autowired
    private FormeJuridiqueRepository formeJuridiqueRepository;

    private static final Logger logger = LoggerFactory.getLogger(EntrepriseService.class);
    public List<String> getAllVilles() {
        return entrepriseRepository.findDistinctVille();
    }
    public Entreprise createEntreprise(Entreprise entreprise) {
        return entrepriseRepository.save(entreprise);
    }

    public List<EntrepriseDTO> getAllEntreprises() {
        List<Entreprise> entreprises = entrepriseRepository.findAll();
        return entreprises.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public Optional<EntrepriseDTO> getEntrepriseById(Long id) {
        return entrepriseRepository.findById(id)
                .map(this::convertToDTO);
    }


    private EntrepriseDTO convertToDTO(Entreprise entreprise) {
        EntrepriseDTO dto = new EntrepriseDTO();
        dto.setId(entreprise.getId());
        dto.setDenomination(entreprise.getDenomination());
        dto.setCapitalSocial(entreprise.getCapitalSocial());
        dto.setIce(entreprise.getIce());
        dto.setIdentifiantFiscal(entreprise.getIdentifiantFiscal());
        dto.setNumRegistreCommerce(entreprise.getNumRegistreCommerce());
        dto.setNumPatente(entreprise.getNumPatente());
        dto.setNumAffiliationCnss(entreprise.getNumAffiliationCnss());
        dto.setAdresse(entreprise.getAdresse());
        dto.setVille(entreprise.getVille());
        dto.setMail(entreprise.getMail());
        dto.setSiteWeb(entreprise.getSiteWeb());
        dto.setNombreEmployes(entreprise.getNombreEmployes());
        dto.setLatitude(entreprise.getLatitude());
        dto.setLongitude(entreprise.getLongitude());
        dto.setDateCreation(entreprise.getDateCreation());
        // Conversion du logo BLOB en Base64
        if (entreprise.getLogo() != null) {
            String base64Logo = Base64.getEncoder().encodeToString(entreprise.getLogo());
            dto.setLogo(base64Logo);
        }
        dto.setDateCessationActivite(entreprise.getDateCessationActivite());

        if (entreprise.getSecteurDactivite() != null) {
            SecteurDactiviteDTO secteurDTO = new SecteurDactiviteDTO();
            secteurDTO.setId(entreprise.getSecteurDactivite().getId());
            secteurDTO.setNom(entreprise.getSecteurDactivite().getNom());
            dto.setSecteurDactivite(secteurDTO);
        }

        if (entreprise.getFormeJuridique() != null) {
            FormeJuridiqueDTO formeDTO = new FormeJuridiqueDTO();
            formeDTO.setId(entreprise.getFormeJuridique().getId());
            formeDTO.setNom(entreprise.getFormeJuridique().getNom());
            dto.setFormeJuridique(formeDTO);
        }

        dto.setTelephones(entreprise.getTelephones().stream()
                .map(t -> {
                    TelephoneDTO tDto = new TelephoneDTO();
                    tDto.setId(t.getId());
                    tDto.setNumero(t.getNumero());
                    return tDto;
                })
                .collect(Collectors.toList()));

        dto.setFaxes(entreprise.getFaxes().stream()
                .map(f -> {
                    FaxDTO fDto = new FaxDTO();
                    fDto.setId(f.getId());
                    fDto.setNumero(f.getNumero());
                    return fDto;
                })
                .collect(Collectors.toList()));

        dto.setGerants(entreprise.getGerants().stream()
                .map(g -> {
                    GerantDTO gDto = new GerantDTO();
                    gDto.setId(g.getId());
                    gDto.setNom(g.getNom());
                    gDto.setPrenom(g.getPrenom());
                    return gDto;
                })
                .collect(Collectors.toList()));

        dto.setHistoriqueDentreprise(entreprise.getHistoriqueDentreprise().stream()
                .map(h -> {
                    HistoriqueDentrepriseDTO hDto = new HistoriqueDentrepriseDTO();
                    hDto.setId(h.getId());
                    hDto.setAttributModifie(h.getAttributModifie());
                    hDto.setAncienneValeur(h.getAncienneValeur());
                    hDto.setNouvelleValeur(h.getNouvelleValeur());
                    hDto.setDateModification(h.getDateModification());
                    return hDto;
                })
                .collect(Collectors.toList()));

        return dto;
    }
    @Transactional
    public EntrepriseDTO updateEntreprise(Long id, Map<String, Object> updates, MultipartFile logo) {
        Optional<Entreprise> optionalEntreprise = entrepriseRepository.findById(id);
        if (optionalEntreprise.isEmpty()) {
            throw new EntityNotFoundException("Entreprise introuvable avec l'id " + id);
        }

        Entreprise entreprise = optionalEntreprise.get();
        Entreprise entrepriseOriginale = new Entreprise();
        BeanUtils.copyProperties(entreprise, entrepriseOriginale);

        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(entreprise);
        ObjectMapper objectMapper = new ObjectMapper();

        updates.forEach((key, value) -> {
            if (value != null) {
                try {
                    switch (key) {
                        case "telephones":
                            List<Telephone> telephones = objectMapper.readValue((String) value, new TypeReference<List<Telephone>>() {});
                            telephoneService.updateTelephonesByEntrepriseId(id, telephones);
                            break;
                        case "faxes":
                            List<Fax> faxes = objectMapper.readValue((String) value, new TypeReference<List<Fax>>() {});
                            faxService.updateFaxesByEntrepriseId(id, faxes);
                            break;
                        case "gerants":
                            List<Gerant> gerants = objectMapper.readValue((String) value, new TypeReference<List<Gerant>>() {});
                            gerantService.updateGerantsByEntrepriseId(id, gerants);
                            break;
                        case "secteurDactivite":
                            Long secteurId = Long.valueOf(value.toString());
                            SecteurDactivite secteur = secteurDactiviteRepository.findById(secteurId)
                                    .orElseThrow(() -> new EntityNotFoundException("Secteur d'activité introuvable avec l'id " + secteurId));
                            entreprise.setSecteurDactivite(secteur);
                            break;
                        case "formeJuridique":
                            Long formeId = Long.valueOf(value.toString());
                            FormeJuridique forme = formeJuridiqueRepository.findById(formeId)
                                    .orElseThrow(() -> new EntityNotFoundException("Forme juridique introuvable avec l'id " + formeId));
                            entreprise.setFormeJuridique(forme);
                            break;
                        case "dateCreation":
                            try {
                                LocalDate dateCreation = LocalDate.parse((String) value);
                                entreprise.setDateCreation(Date.from(dateCreation.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                            } catch (DateTimeParseException e) {
                                throw new RuntimeException("Erreur de format pour dateCreation", e);
                            }
                            break;
                        case "dateCessationActivite":
                            try {
                                LocalDate dateCessation = LocalDate.parse((String) value);
                                entreprise.setDateCessationActivite(Date.from(dateCessation.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                            } catch (DateTimeParseException e) {
                                throw new RuntimeException("Erreur de format pour dateCessationActivite", e);
                            }
                            break;
                        default:
                            beanWrapper.setPropertyValue(key, value);
                            break;
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Erreur lors de la mise à jour des champs: " + key, e);
                }
            }
        });

        if (logo != null) {
            try {
                entreprise.setLogo(logo.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de la lecture du fichier logo", e);
            }
        }

        Entreprise entrepriseMiseAJour = entrepriseRepository.save(entreprise);
        saveHistoryChanges(entrepriseOriginale, entrepriseMiseAJour);

        return convertToDTO(entrepriseMiseAJour);
    }




    private void saveHistoryChanges(Entreprise originalEntreprise, Entreprise updatedEntreprise) {
        BeanWrapper originalWrapper = new BeanWrapperImpl(originalEntreprise);
        BeanWrapper updatedWrapper = new BeanWrapperImpl(updatedEntreprise);

        for (Field field : Entreprise.class.getDeclaredFields()) {
            Object originalValue = originalWrapper.getPropertyValue(field.getName());
            Object updatedValue = updatedWrapper.getPropertyValue(field.getName());

            boolean valuesDiffer = (originalValue != null && !originalValue.equals(updatedValue)) ||
                    (originalValue == null && updatedValue != null);

            if (valuesDiffer) {
                String originalStringValue = originalValue != null ? originalValue.toString() : null;
                String updatedStringValue = updatedValue != null ? updatedValue.toString() : null;

                boolean alreadyExists = historiqueDentrepriseRepository.existsByEntrepriseAndAttributModifieAndAncienneValeurAndNouvelleValeur(
                        updatedEntreprise, field.getName(), originalStringValue, updatedStringValue);

                if (!alreadyExists) {
                    HistoriqueDentreprise historique = new HistoriqueDentreprise();
                    historique.setEntreprise(updatedEntreprise);
                    historique.setAttributModifie(field.getName());
                    historique.setAncienneValeur(originalStringValue);
                    historique.setNouvelleValeur(updatedStringValue);
                    historique.setDateModification(new Date());
                    historiqueDentrepriseRepository.save(historique);
                }
            }
        }
    }




    public void deleteEntreprise(Long id) {
        entrepriseRepository.deleteById(id);
    }
    // Méthode de filtrage des entreprises
    public List<EntrepriseDTO> filterEntreprises(String ville, String denomination, String secteurNom, String formeJuridiqueNom) {
        List<Entreprise> entreprises = entrepriseRepository.filterEntreprises(ville, denomination, secteurNom, formeJuridiqueNom);
        return entreprises.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

}