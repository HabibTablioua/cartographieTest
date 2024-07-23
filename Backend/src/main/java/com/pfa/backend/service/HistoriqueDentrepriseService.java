package com.pfa.backend.service;

import com.pfa.backend.DTO.HistoriqueDentrepriseDTO;
import com.pfa.backend.entity.HistoriqueDentreprise;
import com.pfa.backend.repository.HistoriqueDentrepriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoriqueDentrepriseService {

    @Autowired
    private HistoriqueDentrepriseRepository historiqueDentrepriseRepository;
    public HistoriqueDentreprise save(HistoriqueDentreprise historiqueDentreprise) {
        return historiqueDentrepriseRepository.save(historiqueDentreprise);
    }
    public List<HistoriqueDentreprise> getAllHistoriques() {
        return historiqueDentrepriseRepository.findAll();
    }

    public Optional<HistoriqueDentreprise> getHistoriqueById(Long id) {
        return historiqueDentrepriseRepository.findById(id);
    }


    public List<HistoriqueDentrepriseDTO> getHistoriqueByEntrepriseId(Long entrepriseId) {
        List<HistoriqueDentreprise> historiqueEntities = historiqueDentrepriseRepository.findByEntrepriseId(entrepriseId);
        return historiqueEntities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private HistoriqueDentrepriseDTO convertToDTO(HistoriqueDentreprise historique) {
        HistoriqueDentrepriseDTO dto = new HistoriqueDentrepriseDTO();
        dto.setId(historique.getId());
        dto.setAttributModifie(historique.getAttributModifie());
        dto.setAncienneValeur(historique.getAncienneValeur());
        dto.setNouvelleValeur(historique.getNouvelleValeur());
        dto.setDateModification(historique.getDateModification());
        return dto;
    }
}
