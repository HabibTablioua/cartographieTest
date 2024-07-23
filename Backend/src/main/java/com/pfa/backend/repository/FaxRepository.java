package com.pfa.backend.repository;

import com.pfa.backend.entity.Fax;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaxRepository extends JpaRepository<Fax, Long> {
    List<Fax> findByEntrepriseId(Long entrepriseId);
}
