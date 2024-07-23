package com.pfa.backend.repository;

import com.pfa.backend.entity.Telephone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TelephoneRepository extends JpaRepository<Telephone, Long> {
    List<Telephone> findByEntrepriseId(Long entrepriseId);
}
