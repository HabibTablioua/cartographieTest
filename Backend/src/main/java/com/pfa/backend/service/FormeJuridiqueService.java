package com.pfa.backend.service;

import com.pfa.backend.DTO.FormeJuridiqueDTO;
import com.pfa.backend.entity.FormeJuridique;
import com.pfa.backend.repository.FormeJuridiqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FormeJuridiqueService {

    @Autowired
    private FormeJuridiqueRepository formeJuridiqueRepository;

    public List<FormeJuridiqueDTO> getAllFormesJuridiques() {
        // Récupérer toutes les entités FormeJuridique
        List<FormeJuridique> formesJuridiques = formeJuridiqueRepository.findAll();

        // Convertir les entités en DTO et les retourner
        return formesJuridiques.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<FormeJuridiqueDTO> getFormeJuridiqueById(Long id) {
        return formeJuridiqueRepository.findById(id)
                .map(this::convertToDTO);
    }

    private FormeJuridiqueDTO convertToDTO(FormeJuridique formeJuridique) {
        FormeJuridiqueDTO dto = new FormeJuridiqueDTO();
        dto.setId(formeJuridique.getId());
        dto.setNom(formeJuridique.getNom());
        return dto;
    }
    public FormeJuridique createFormeJuridique(FormeJuridique formeJuridique) {
        return formeJuridiqueRepository.save(formeJuridique);
    }

    public void deleteFormeJuridique(Long id) {
        formeJuridiqueRepository.deleteById(id);
    }
}
