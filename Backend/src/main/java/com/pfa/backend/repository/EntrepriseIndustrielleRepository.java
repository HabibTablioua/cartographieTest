package com.pfa.backend.repository;

import com.pfa.backend.entity.EntrepriseIndustrielle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntrepriseIndustrielleRepository extends JpaRepository<EntrepriseIndustrielle, Long> {
}
