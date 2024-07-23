package com.pfa.backend.repository;

import com.pfa.backend.entity.Gerant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GerantRepository extends JpaRepository<Gerant, Long> {
    List<Gerant> findByEntrepriseId(Long entrepriseId);
}
