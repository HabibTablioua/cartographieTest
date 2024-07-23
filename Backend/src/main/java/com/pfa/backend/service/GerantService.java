package com.pfa.backend.service;

import com.pfa.backend.entity.Gerant;
import com.pfa.backend.entity.Entreprise;
import com.pfa.backend.repository.GerantRepository;
import com.pfa.backend.repository.EntrepriseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GerantService {

    @Autowired
    private GerantRepository gerantRepository;

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    public List<Gerant> getGerantsByEntrepriseId(Long entrepriseId) {
        return gerantRepository.findByEntrepriseId(entrepriseId);
    }

    public Optional<Gerant> findById(Long id) {
        return gerantRepository.findById(id);
    }

    public Gerant save(Gerant gerant) {
        return gerantRepository.save(gerant);
    }

    public void deleteById(Long id) {
        gerantRepository.deleteById(id);
    }

    @Transactional
    public List<Gerant> updateGerantsByEntrepriseId(Long entrepriseId, List<Gerant> updatedGerants) {
        Optional<Entreprise> optionalEntreprise = entrepriseRepository.findById(entrepriseId);
        if (optionalEntreprise.isEmpty()) {
            throw new EntityNotFoundException("Entreprise introuvable avec l'id " + entrepriseId);
        }

        Entreprise entreprise = optionalEntreprise.get();
        List<Gerant> existingGerants = gerantRepository.findByEntrepriseId(entrepriseId);

        // Mettre à jour les gérants existants ou les ajouter si nouveaux
        for (Gerant updatedGerant : updatedGerants) {
            Optional<Gerant> optionalExistingGerant = existingGerants.stream()
                    .filter(g -> g.getId().equals(updatedGerant.getId()))
                    .findFirst();

            if (optionalExistingGerant.isPresent()) {
                Gerant existingGerant = optionalExistingGerant.get();
                existingGerant.setNom(updatedGerant.getNom());
                existingGerant.setPrenom(updatedGerant.getPrenom());
                gerantRepository.save(existingGerant);
            } else {
                updatedGerant.setEntreprise(entreprise);
                gerantRepository.save(updatedGerant);
            }
        }



        return gerantRepository.findByEntrepriseId(entrepriseId);
    }
}