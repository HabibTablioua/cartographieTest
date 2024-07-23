package com.pfa.backend.repository;

import com.pfa.backend.entity.Entreprise;
import com.pfa.backend.entity.EntrepriseCommerciale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntrepriseCommercialeRepository extends JpaRepository<EntrepriseCommerciale, Long> {
    List<Entreprise> findByVille(String ville);
    List<Entreprise> findByDenominationContaining(String denomination);
    List<Entreprise> findBySecteurDactiviteId(Long secteurId);
    List<Entreprise> findByFormeJuridiqueId(Long formeJuridiqueId);
}
