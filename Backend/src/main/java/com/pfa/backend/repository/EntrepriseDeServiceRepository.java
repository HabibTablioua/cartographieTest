package com.pfa.backend.repository;

import com.pfa.backend.entity.EntrepriseDeService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntrepriseDeServiceRepository extends JpaRepository<EntrepriseDeService, Long> {
}
