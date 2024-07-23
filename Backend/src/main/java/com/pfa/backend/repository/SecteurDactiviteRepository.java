package com.pfa.backend.repository;

import com.pfa.backend.entity.SecteurDactivite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecteurDactiviteRepository extends JpaRepository<SecteurDactivite, Long> {
}
