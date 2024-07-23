package com.pfa.backend.repository;

import com.pfa.backend.entity.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {

    @Query("SELECT DISTINCT e.ville FROM Entreprise e WHERE e.ville IS NOT NULL")
    List<String> findDistinctVille();

    @Query("SELECT e FROM Entreprise e " +
            "LEFT JOIN e.secteurDactivite s " +
            "LEFT JOIN e.formeJuridique f " +
            "WHERE (:ville IS NULL OR :ville = '' OR e.ville = :ville) " +
            "AND (:denomination IS NULL OR :denomination = '' OR e.denomination LIKE %:denomination%) " +
            "AND (:secteurNom IS NULL OR :secteurNom = '' OR s.nom = :secteurNom) " +
            "AND (:formeJuridiqueNom IS NULL OR :formeJuridiqueNom = '' OR f.nom = :formeJuridiqueNom) " +
            "AND (COALESCE(:ville, '') = '' OR COALESCE(:denomination, '') = '' OR COALESCE(:secteurNom, '') = '' OR COALESCE(:formeJuridiqueNom, '') = '' " +
            "    OR (e.ville = :ville AND e.denomination LIKE %:denomination% AND s.nom = :secteurNom AND f.nom = :formeJuridiqueNom))")
    List<Entreprise> filterEntreprises(
            @Param("ville") String ville,
            @Param("denomination") String denomination,
            @Param("secteurNom") String secteurNom,
            @Param("formeJuridiqueNom") String formeJuridiqueNom
    );
}
