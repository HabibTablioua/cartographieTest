package com.pfa.backend.repository;

import com.pfa.backend.entity.Entreprise;
import com.pfa.backend.entity.HistoriqueDentreprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriqueDentrepriseRepository extends JpaRepository<HistoriqueDentreprise, Long> {
    List<HistoriqueDentreprise> findByEntrepriseId(Long entrepriseId);
    boolean existsByEntrepriseAndAttributModifieAndAncienneValeurAndNouvelleValeur(
            Entreprise entreprise, String attributModifie, String ancienneValeur, String nouvelleValeur);
}