package com.pfa.backend.service;

import com.pfa.backend.entity.Entreprise;
import com.pfa.backend.entity.Telephone;
import com.pfa.backend.repository.EntrepriseRepository;
import com.pfa.backend.repository.TelephoneRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TelephoneService {
    @Autowired
    private EntrepriseRepository entrepriseRepository;
    @Autowired
    private TelephoneRepository telephoneRepository;
    public List<Telephone> getTelephonesByEntrepriseId(Long entrepriseId) {
        return telephoneRepository.findByEntrepriseId(entrepriseId);
    }
    public Optional<Telephone> findById(Long id) {
        return telephoneRepository.findById(id);
    }

    public Telephone save(Telephone telephone) {
        return telephoneRepository.save(telephone);
    }

    public void deleteById(Long id) {
        telephoneRepository.deleteById(id);
    }

    @Transactional
    public List<Telephone> updateTelephonesByEntrepriseId(Long entrepriseId, List<Telephone> updatedTelephones) {
        Optional<Entreprise> optionalEntreprise = entrepriseRepository.findById(entrepriseId);
        if (optionalEntreprise.isEmpty()) {
            throw new EntityNotFoundException("Entreprise introuvable avec l'id " + entrepriseId);
        }

        Entreprise entreprise = optionalEntreprise.get();
        List<Telephone> existingTelephones = telephoneRepository.findByEntrepriseId(entrepriseId);

        // Mettre à jour les téléphones existants ou les ajouter si nouveaux
        for (Telephone updatedTelephone : updatedTelephones) {
            Optional<Telephone> optionalExistingTelephone = existingTelephones.stream()
                    .filter(t -> t.getId().equals(updatedTelephone.getId()))
                    .findFirst();

            if (optionalExistingTelephone.isPresent()) {
                Telephone existingTelephone = optionalExistingTelephone.get();
                existingTelephone.setNumero(updatedTelephone.getNumero());
                telephoneRepository.save(existingTelephone);
            } else {
                updatedTelephone.setEntreprise(entreprise);
                telephoneRepository.save(updatedTelephone);
            }
        }



        return telephoneRepository.findByEntrepriseId(entrepriseId);
    }

}