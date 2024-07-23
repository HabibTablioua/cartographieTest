package com.pfa.backend.service;

import com.pfa.backend.DTO.SecteurDactiviteDTO;
import com.pfa.backend.entity.SecteurDactivite;
import com.pfa.backend.repository.SecteurDactiviteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SecteurDactiviteService {

    @Autowired
    private SecteurDactiviteRepository secteurDactiviteRepository;

    public List<SecteurDactiviteDTO> getAllSecteursDactivite() {
        // Récupérer toutes les entités SecteurDactivite
        List<SecteurDactivite> secteursDactivite = secteurDactiviteRepository.findAll();

        // Convertir les entités en DTO et les retourner
        return secteursDactivite.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<SecteurDactiviteDTO> getSecteurDactiviteById(Long id) {
        return secteurDactiviteRepository.findById(id)
                .map(this::convertToDTO);
    }

    private SecteurDactiviteDTO convertToDTO(SecteurDactivite secteurDactivite) {
        SecteurDactiviteDTO dto = new SecteurDactiviteDTO();
        dto.setId(secteurDactivite.getId());
        dto.setNom(secteurDactivite.getNom());
        return dto;
    }
    public SecteurDactivite createSecteurDactivite(SecteurDactivite secteurDactivite) {
        return secteurDactiviteRepository.save(secteurDactivite);
    }

    public void deleteSecteurDactivite(Long id) {
        secteurDactiviteRepository.deleteById(id);
    }
}
